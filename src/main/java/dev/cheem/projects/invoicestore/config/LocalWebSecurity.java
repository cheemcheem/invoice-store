package dev.cheem.projects.invoicestore.config;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile("local")
@Configuration
@Slf4j
public class LocalWebSecurity extends WebSecurityConfigurerAdapter {

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
            .logoutSuccessHandler(
                (request, response, authentication) -> handleSuccess(request, response))
        )
        .oauth2Login(o -> o
            .successHandler((request, response, authentication) -> handleSuccess(request, response))
        )
    ;
  }

  private void handleSuccess(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    var referrer = request.getHeader("referer");
    log.info("referrer '{}' ", referrer);
    if (Objects.nonNull(referrer)) {
      var uri = URI.create(referrer);
      response.sendRedirect("http://" + uri.getHost() + ":" + uri.getPort());
    }
  }
}
