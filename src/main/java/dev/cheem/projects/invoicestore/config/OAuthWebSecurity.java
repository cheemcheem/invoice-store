package dev.cheem.projects.invoicestore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Order(2)
//@Profile("oauth")
//@Configuration
public class OAuthWebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
//        .authorizeRequests(a -> a
////            .antMatchers(HttpMethod.GET, "/api/invoice/**").anonymous()
////            .anyRequest().authenticated()
//        )
        .oauth2Login(o -> o.loginPage("/login"))
    ;
  }
}
