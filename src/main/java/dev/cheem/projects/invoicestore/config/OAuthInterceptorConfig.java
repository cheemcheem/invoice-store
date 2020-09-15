package dev.cheem.projects.invoicestore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("oauth")
@RequiredArgsConstructor
@Configuration
public class OAuthInterceptorConfig implements WebMvcConfigurer {

  private final OAuthInterceptor OAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(OAuthInterceptor);
  }

}
