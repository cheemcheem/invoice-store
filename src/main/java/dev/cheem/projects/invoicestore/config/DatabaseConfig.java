package dev.cheem.projects.invoicestore.config;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@Slf4j
public class DatabaseConfig {

  @Value("${DB_MAX_USER_LIMIT}")
  private String maxUserLimit;

  private Integer maxUserLimit() {
    return Integer.valueOf(maxUserLimit);
  }

  @Bean
  public DatabaseInstance databaseInstance() {
    return new DatabaseInstance(
        maxUserLimit()
    );
  }

  @Data
  public static class DatabaseInstance {

    private final Integer maxUserLimit;
  }
}
