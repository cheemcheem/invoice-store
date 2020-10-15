package dev.cheem.projects.invoicestore.config.profile.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile("oauth2")
@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebSecurityOAuth2 extends WebSecurityConfigurerAdapter {

  private final Environment environment;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    var local = List.of(environment.getActiveProfiles()).contains("local");

    LogoutSuccessHandler logoutSuccessHandler = local
        ? this::handleLocalSuccess
        : this::handleRemoteSuccess;
    AuthenticationSuccessHandler authenticationSuccessHandler = local
        ? this::handleLocalSuccess
        : this::handleRemoteSuccess;

    //noinspection SpellCheckingInspection
    http.authorizeRequests(a -> a
        .antMatchers("/**/*.{js,html,css,ico,png,webmanifest,svg,xml}").permitAll()
        .antMatchers("/", "/error").permitAll()
        .antMatchers("/login", "/all", "/view/**/*", "/new", "/archived", "/privacy").permitAll()
        .antMatchers("/api/user").permitAll()
        .anyRequest().authenticated()
    ).csrf(c -> c
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    ).logout(l -> l
        .logoutUrl("/logout")
        .logoutSuccessHandler(logoutSuccessHandler)
    ).oauth2Login(o -> o
        .successHandler(authenticationSuccessHandler)
    );
  }

  private void handleLocalSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    var referrer = request.getHeader("referer");
    if (Objects.nonNull(referrer)) {
      var uri = URI.create(referrer);
      response.sendRedirect("http://" + uri.getHost() + ":" + uri.getPort());
    }
  }

  private void handleRemoteSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    response.sendRedirect("/");
  }
}
