package dev.cheem.projects.invoicestore.config;

import java.net.URI;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

@Profile("local")
@Configuration
@Slf4j
public class LocalWebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .logout(l -> l
            .logoutSuccessHandler((request, response, authentication) -> {
              var referrer = request.getHeader("referer");
              log.info("referrer '{}' ", referrer);
              if (Objects.nonNull(referrer)) {
                var uri = URI.create(referrer);
                response.sendRedirect("http://" + uri.getHost() + ":" + uri.getPort());
              }
            })
        )
        .oauth2Login(o -> o
            .successHandler((request, response, authentication) -> {
              var referrer = request.getHeader("referer");
              log.info("referrer '{}' ", referrer);
              if (Objects.nonNull(referrer)) {
                var uri = URI.create(referrer);
                response.sendRedirect("http://" + uri.getHost() + ":" + uri.getPort());
              }
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
