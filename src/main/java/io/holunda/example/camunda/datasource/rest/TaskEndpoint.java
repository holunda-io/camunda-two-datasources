package io.holunda.example.camunda.datasource.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.holunda.example.camunda.datasource.domain.BusinessService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(REST.REST_PREFIX)
public class TaskEndpoint {

    private final BusinessService businessService;
    private final TaskService taskService;

    public TaskEndpoint(BusinessService businessService, TaskService taskService) {
        this.businessService = businessService;
        this.taskService = taskService;
    }

    @GetMapping(value = "/task", produces = "application/json")
    public ResponseEntity<List<String>> getTasks() {
        return ok().body(
            taskService.createTaskQuery().list().stream().map(Task::getId).collect(Collectors.toList())
        );
    }


    @PostMapping(value = "/task/{taskId}", produces = "application/json")
    public ResponseEntity<Void> completeTask(@PathVariable("taskId") String taskId, @RequestBody TaskCompletionDto dto) {
        try {
            businessService.completeTask(taskId, dto.shouldFail);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class TaskCompletionDto {
        @JsonProperty("shouldFail")
        public boolean shouldFail;
    }
}


