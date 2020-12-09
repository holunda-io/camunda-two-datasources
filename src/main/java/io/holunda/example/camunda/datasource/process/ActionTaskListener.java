package io.holunda.example.camunda.datasource.process;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("loggingListener")
public class ActionTaskListener implements TaskListener {

    private final static Logger logger = LoggerFactory.getLogger(ActionTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        logger.info("Task created with id: " + delegateTask.getId());
    }
}
