package io.holunda.example.camunda.datasource.config;

import io.holunda.example.camunda.datasource.domain.Action;
import io.holunda.example.camunda.datasource.domain.ActionRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Datasource configuration for Spring profile "secondary".
 */
@Profile("secondary")
@Configuration
@EnableJpaRepositories(
    basePackageClasses = ActionRepository.class,
    transactionManagerRef = "camundaBpmTransactionManager",
    entityManagerFactoryRef = "camundaBpmEntityManagerFactory"
)
public class ProfileSecondaryConfiguration {

    @Bean(name = "camundaBpmDataSource")
    @ConfigurationProperties(prefix="spring.second.datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "camundaBpmEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEMFactory(@Qualifier("camundaBpmDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder, JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        var properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
        return builder
            .dataSource(dataSource)
            .packages(Action.class)
            .persistenceUnit("second")
            .properties(properties)
            .build();
    }

    @Bean(name = "camundaBpmTransactionManager")
    public PlatformTransactionManager secondTransactionManager(@Qualifier("camundaBpmEntityManagerFactory") FactoryBean<EntityManagerFactory> emFactory) throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emFactory.getObject());
        return transactionManager;
    }
}
