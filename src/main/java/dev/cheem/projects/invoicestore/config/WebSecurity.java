package dev.cheem.projects.invoicestore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile("!local")
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests(a -> a
            .antMatchers("/**/*.{js,html,css}").permitAll()
            .antMatchers("/", "/error").permitAll()
            .anyRequest().authenticated()
        )
        .logout(l -> l
            .logoutSuccessUrl("/").permitAll()
        )
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .oauth2Login();

  }
}
