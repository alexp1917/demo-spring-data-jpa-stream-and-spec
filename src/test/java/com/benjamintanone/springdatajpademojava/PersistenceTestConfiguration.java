package com.benjamintanone.springdatajpademojava;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.mvnsearch.h2.H2FunctionsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;


@Slf4j
// @TestConfiguration
@Configuration
public class PersistenceTestConfiguration {

    @Primary
    @Bean
    @Qualifier("myPrimaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProps() {
        log.info("hello");
        return new LiquibaseProperties();
    }

    // @Bean
    // public void setUpH2WithMysqlFunctions(DataSource primaryDataSource) throws SQLException {
    // }

    @Bean
    public SpringLiquibase springLiquibase(DataSource myPrimaryDataSource, LiquibaseProperties liquibaseProps) throws SQLException {
        H2FunctionsLoader.loadMysqlFunctions(primaryDataSource());
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(myPrimaryDataSource);
        liquibase.setChangeLog(liquibaseProps.getChangeLog());
        liquibase.setContexts(liquibaseProps.getContexts());
        liquibase.setDefaultSchema(liquibaseProps.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProps.isDropFirst());
        liquibase.setShouldRun(liquibaseProps.isEnabled());
        liquibase.setLabels(liquibaseProps.getLabels());
        liquibase.setChangeLogParameters(liquibaseProps.getParameters());
        liquibase.setRollbackFile(liquibaseProps.getRollbackFile());
        return liquibase;
    }

}
