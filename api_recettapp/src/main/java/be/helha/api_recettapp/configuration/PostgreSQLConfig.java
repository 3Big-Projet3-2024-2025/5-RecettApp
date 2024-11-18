package be.helha.api_recettapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "be.helha.api_recettapp.repositories.jpa"
)
public class PostgreSQLConfig {
}
