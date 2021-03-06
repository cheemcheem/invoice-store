package dev.cheem.projects.invoicestore.config.profile.oauth2;

import static dev.cheem.projects.invoicestore.util.Constants.DO_NOT_INTERCEPT;

import dev.cheem.projects.invoicestore.service.UserService;
import dev.cheem.projects.invoicestore.util.Constants;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.util.NumberUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Profile("oauth2")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcInterceptorOAuth2 implements HandlerInterceptor {

  private final UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws IOException {
    log.debug("WebMvcInterceptorProd.preHandle");
    log.debug(request.getServletPath());

    if (DO_NOT_INTERCEPT.test(request.getServletPath())) {
      return true;
    }

    var userIdObject = request.getSession().getAttribute(Constants.USER_ID_SESSION_KEY);

    if (Objects.isNull(userIdObject)) {
      log.debug("User id in session is null.");

      var principal = request.getUserPrincipal();

      if (Objects.isNull(principal)) {
        log.debug("Principal is null.");
        return false;
      }

      if (Objects.isNull(principal.getName())) {
        log.debug("User id in principal is null.");
        return false;
      }

      if (!userService.allowedMoreUsers()) {
        log.warn("Max user limit reached.");
        response.sendError(HttpStatus.INSUFFICIENT_STORAGE.value(), "Too many users registered.");
        return false;
      }

      var userId = userService.login(principal.getName()).getUserId();

      request.getSession().setAttribute(Constants.USER_ID_SESSION_KEY, userId);
      userIdObject = request.getSession().getAttribute(Constants.USER_ID_SESSION_KEY);

    }

    var userId = NumberUtils.parseNumber(
        userIdObject.toString(),
        Long.class
    );

    log.debug("Looking for user {}.", userId);
    var optionalUser = userService.checkUser(userId);
    if (optionalUser.isEmpty()) {
      log.debug("Failed to get user.");
      return false;
    }

    log.debug("Found user {}.", optionalUser.get());

    request.setAttribute(Constants.USER_ID_ATTRIBUTE_KEY, optionalUser.get());
    return true;
  }
}

