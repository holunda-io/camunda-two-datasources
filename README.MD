# Spring Two Datasource Configuration for a Process Application

## Goal

Demonstration of Camunda BPM Spring Boot integrating multiple data sources and atomicity
of operations on Spring Data JPA and the Engine.

## Idea

There is a small process creating a user task. A REST controller exposes an endpoint which can
be used to complete this task. By doing so, you need to pass a boolean parameter evaluated inside
a transaction `BusinessService`. The `BusinessService` writes a JPA entity to a database and
completes a user task in one transaction. If the passed flag evaluates to true this method will fail,
the transaction gets rolled-back. Especially, the user task should remain in place and the entity 
should not be saved to database.

## Demonstration

There is a SpringBoot Test using MockMVC to interact with the REST endpoints validating that the system
behaves correctly for both values of the parameter. To show different configuration, several Spring
Profiles are created, which are coupled to Maven profiles. To run it, please specify the profile 
with `-P<name>` flag in your command line. To run the test, please run `mvn clean verify` 

The profiles are:

* none:       just one in-mem h2 is created
* explicit:   the in-mem h2 is specified in properties
* multiple:   there are two data sources defined in the properties, but the first (default) one should be used
* secondary:  there are two data sources defined in the properties, but the second one should be used



 