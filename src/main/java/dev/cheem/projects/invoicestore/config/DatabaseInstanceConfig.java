package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.model.DatabaseInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class DatabaseInstanceConfig {

  private final int maxUserLimit;

  @Bean
  public DatabaseInstance databaseInstance() {
    return new DatabaseInstance(maxUserLimit);
  }
}
