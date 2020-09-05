package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.repository.UserRepository;
import dev.cheem.projects.invoicestore.util.Constants;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Profile("local")
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

  private final UserRepository userRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    log.debug("AuthInterceptor.preHandle");
    log.debug(request.getServletPath());

    var defaultUserId = 1;
    var optionalUser = userRepository.findById(defaultUserId);
    if (optionalUser.isEmpty()) {
      log.debug("Failed to get default user.");
      return false;
    }
    request.setAttribute(Constants.USER_ID_ATTRIBUTE_KEY, optionalUser.get());
    return true;
  }
}

