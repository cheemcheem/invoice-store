package dev.cheem.projects.invoicestore.config.profile.none;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration("databaseConfig")
@Slf4j
public class DatabaseConfig {
  @Bean
  public Integer maxUserLimit() {
    return 100;
  }
}
