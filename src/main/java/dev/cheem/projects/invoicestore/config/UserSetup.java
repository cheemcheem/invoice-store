package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!oauth2")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserSetup implements CommandLineRunner {

  private final UserRepository userRepository;

  @Override
  public void run(String... args) {
    var user = new User();
    user.setOAuth2Id("Fake OAuth2 ID");
    user.setUserId(1L);
    userRepository.save(user);
  }

}
