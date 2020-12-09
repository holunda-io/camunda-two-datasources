package io.holunda.example.camunda.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.example.camunda.datasource.domain.ActionRepository;
import io.holunda.example.camunda.datasource.rest.REST;
import io.holunda.example.camunda.datasource.rest.TaskEndpoint;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessEngineTaskCompletionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActionRepository repository;

    @AfterEach
    @Transactional
    public void cleanup() {
        List<String> all = runtimeService.createProcessInstanceQuery().list().stream().map(ProcessInstance::getProcessInstanceId).collect(Collectors.toList());
        runtimeService.deleteProcessInstances(all, "test cleanup", true, true);
        repository.deleteAll();
        repository.flush();
    }


    @Test
    public void should_start_process_and_complete_user_task() throws Exception {

        mockMvc.perform(
            post(REST.REST_PREFIX + "/start")
                .servletPath("")
        ).andExpect(
            status().isCreated()
        );

        mockMvc.perform(
            get(REST.REST_PREFIX + "/task")
                .servletPath("")
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(json().isPresent())
               .andExpect(json().isArray());

        Task task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNotNull();

        mockMvc.perform(
            post(REST.REST_PREFIX + "/task/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskCompletionJson(false))
                .servletPath("")

        ).andExpect(
            status().isNoContent()
        );

        task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNull();

        assertThat(repository.count()).isEqualTo(1);

    }

    @Test
    public void should_start_process_and_fail_to_complete_in_user_task() throws Exception {

        mockMvc.perform(
            post(REST.REST_PREFIX + "/start")
                .servletPath("")
        ).andExpect(
            status().isCreated()
        );

        mockMvc.perform(
            get(REST.REST_PREFIX + "/task")
                .servletPath("")
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(json().isPresent())
               .andExpect(json().isArray());

        Task task = taskService.createTaskQuery().singleResult();
        String taskId = task.getId();
        assertThat(task).isNotNull();

        mockMvc.perform(
            post(REST.REST_PREFIX + "/task/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskCompletionJson(true))
                .servletPath("")

        ).andExpect(
            status().isBadRequest()
        );

        task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(taskId);

        assertThat(repository.count()).isEqualTo(0);

    }

    private static String taskCompletionJson(boolean shouldFail) throws JsonProcessingException {
        TaskEndpoint.TaskCompletionDto dto = new TaskEndpoint.TaskCompletionDto();
        dto.shouldFail = shouldFail;
        return new ObjectMapper().writeValueAsString(dto);
    }
}
