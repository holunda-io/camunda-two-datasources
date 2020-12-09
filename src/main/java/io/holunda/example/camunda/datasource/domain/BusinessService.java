package io.holunda.example.camunda.datasource.domain;

import org.camunda.bpm.engine.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    private final TaskService taskService;
    private final ActionRepository actionRepository;

    public BusinessService(TaskService taskService, ActionRepository actionRepository) {
        this.taskService = taskService;
        this.actionRepository = actionRepository;
    }

    public void completeTask(String taskId, boolean shouldFail) {

        logger.info("Completing task " + taskId);

        Action action = new Action();
        action.setName("Action confirmed by task " + taskId);

        this.actionRepository.save(action);
        this.taskService.complete(taskId);

        if (shouldFail) {
            throw new IllegalArgumentException("The client requested me to fail, so I do so for task " + taskId);
        }


    }
}
