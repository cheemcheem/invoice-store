package dev.cheem.projects.invoicestore.controller;

import dev.cheem.projects.invoicestore.dto.UserDTO;
import dev.cheem.projects.invoicestore.service.UserService;
import dev.cheem.projects.invoicestore.util.Constants;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<UserDTO> user(@AuthenticationPrincipal OAuth2User principal) {
    log.info("UserController.user");
    log.debug("principal = " + principal);
    if (Objects.isNull(principal)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userService.login(principal.getName()));
  }

  @PostMapping
  public ResponseEntity<UserDTO> login(
      @AuthenticationPrincipal OAuth2User principal,
      HttpServletRequest request
  ) {
    log.info("UserController.login");
    log.debug("principal = " + principal + ", request = " + request);
    var login = userService.login(principal.getName());
    log.debug("login '{}' ", login);
    request.getSession().setAttribute(Constants.USER_ID_SESSION_KEY, login.getUserId());
    return ResponseEntity.ok(login);
  }

}
