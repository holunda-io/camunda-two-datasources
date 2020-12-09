package io.holunda.example.camunda.datasource.rest;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(REST.REST_PREFIX)
public class ProcessEndpoint {

    private static final Logger logger = Logger.getLogger(ProcessEndpoint.class);

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public ProcessEndpoint(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @PostMapping(value = "/start", produces = "application/json")
    public ResponseEntity<Void> startProcess() {
        ProcessInstance instance = this.runtimeService.startProcessInstanceByKey("process_simple");
        Task task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        return ResponseEntity
            .created(
                UriComponentsBuilder
                    .fromPath(REST.REST_PREFIX + "/task")
                    .pathSegment("{id}")
                    .build(task.getId())
            ).build();
    }
}