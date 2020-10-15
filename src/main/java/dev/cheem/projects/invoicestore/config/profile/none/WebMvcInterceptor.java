package dev.cheem.projects.invoicestore.config.profile.none;

import static dev.cheem.projects.invoicestore.util.Constants.DO_NOT_INTERCEPT;

import dev.cheem.projects.invoicestore.util.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Profile("!oauth2")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    log.debug("WebMvcInterceptor.preHandle");
    log.debug(request.getServletPath());

    if (DO_NOT_INTERCEPT.test(request.getServletPath())) {
      log.debug("Not intercepting.");
      return true;
    }

    RequestContextHolder.currentRequestAttributes().setAttribute(
        Constants.USER_ID_SESSION_KEY,
        "1",
        RequestAttributes.SCOPE_SESSION
    );

    request.setAttribute(Constants.USER_ID_ATTRIBUTE_KEY, 1L);
    return true;
  }
}
