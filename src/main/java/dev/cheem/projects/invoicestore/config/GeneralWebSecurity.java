package dev.cheem.projects.invoicestore.config;

import java.net.URI;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

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

  @Bean
  public RequestCache refererRequestCache() {
    return new HttpSessionRequestCache() {
      @Override
      public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        String referrer = request.getHeader("referer");
        if (referrer != null) {
          request.getSession()
              .setAttribute("SPRING_SECURITY_SAVED_REQUEST", new SimpleSavedRequest(referrer));
        }
      }
    };
  }
}
