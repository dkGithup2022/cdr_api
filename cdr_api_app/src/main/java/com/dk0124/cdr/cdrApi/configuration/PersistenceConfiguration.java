package com.dk0124.cdr.cdrApi.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(value = "com.dk0124.cdr.persistence")
@EnableJpaRepositories(value = "com.dk0124.cdr.persistence")
@EntityScan(value = "com.dk0124.cdr.persistence")
public class PersistenceConfiguration {
}
