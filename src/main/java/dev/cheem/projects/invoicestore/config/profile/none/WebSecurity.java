package dev.cheem.projects.invoicestore.config.profile.none;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile("!oauth2")
@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(a -> a
        .anyRequest().permitAll()
    ).csrf(c -> c
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    );
  }
}
