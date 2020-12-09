package io.holunda.example.camunda.datasource.rest;

import io.holunda.example.camunda.datasource.domain.Action;
import io.holunda.example.camunda.datasource.domain.ActionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(REST.REST_PREFIX)
public class ActionEndpoint {

    private final ActionRepository repository;

    public ActionEndpoint(ActionRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/action")
    public ResponseEntity<List<String>> getActions() {
        return ok()
            .body(
                this.repository.findAll().stream().map(Action::getName).collect(Collectors.toList())
            );
    }
}
