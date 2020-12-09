package io.holunda.example.camunda.datasource;

import io.holunda.example.camunda.datasource.domain.ActionRepository;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableProcessApplication
@EnableJpaRepositories(excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        ActionRepository.class
    })
})
public class CamundaTwoDatasourceExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamundaTwoDatasourceExampleApplication.class, args);
    }
}
