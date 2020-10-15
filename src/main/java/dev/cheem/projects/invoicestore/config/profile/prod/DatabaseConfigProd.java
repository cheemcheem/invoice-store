package dev.cheem.projects.invoicestore.config.profile.prod;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Setter
@Configuration("databaseConfig")
@Slf4j
public class DatabaseConfigProd {

  @Value("${DB_MAX_USER_LIMIT}")
  private String maxUserLimit;

  @Bean
  public Integer maxUserLimit() {
    return Integer.valueOf(maxUserLimit);
  }

}
