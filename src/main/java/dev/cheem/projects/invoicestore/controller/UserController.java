package dev.cheem.projects.invoicestore.controller;

import dev.cheem.projects.invoicestore.dto.UserDTO;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  @GetMapping
  public ResponseEntity<UserDTO> user(@AuthenticationPrincipal OAuth2User principal) {
    if (Objects.isNull(principal)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(UserDTO.builder()
        .fullName(principal.getAttribute("name"))
        .userName(principal.getAttribute("login"))
        .build());
  }
}
