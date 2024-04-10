package io.frogmi.earthquakes.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.frogmi.earthquakes.domain")
@EnableJpaRepositories("io.frogmi.earthquakes.repos")
@EnableTransactionManagement
public class DomainConfig {
}
