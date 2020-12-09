package io.holunda.example.camunda.datasource.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple JPA Repository.
 */
public interface ActionRepository extends JpaRepository<Action, String> {
}
