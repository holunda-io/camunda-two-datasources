package io.holunda.example.camunda.datasource.config;

import io.holunda.example.camunda.datasource.domain.ActionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile({"!secondary"})
@Configuration
@EnableJpaRepositories(basePackageClasses = ActionRepository.class)
public class DefaultConfiguration {

}
