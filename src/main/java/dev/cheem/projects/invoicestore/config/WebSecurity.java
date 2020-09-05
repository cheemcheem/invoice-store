package dev.cheem.projects.invoicestore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev")
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests(a -> a
            .antMatchers("/**/*").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(c -> c
            .disable()
        )
    ;

  }
}
