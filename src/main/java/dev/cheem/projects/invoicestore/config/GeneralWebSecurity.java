package dev.cheem.projects.invoicestore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Order(1)
@Configuration
@Slf4j
public class GeneralWebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //noinspection SpellCheckingInspection
    http
        .authorizeRequests(a -> a
            .antMatchers("/**/*.{js,html,css,ico,png,webmanifest,svg,xml}").permitAll()
            .antMatchers("/", "/error").permitAll()
            .antMatchers("/login", "/all", "/view/**/*", "/new", "/archived").permitAll()
            .antMatchers("/api/user").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .logout(l -> l
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
        )
        .oauth2Login(o -> o
            .successHandler((request, response, authentication) -> {
              response.sendRedirect("/");
            })
        )
    ;
  }
}
