package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile("local")
public class RegisterDefaultUser implements org.springframework.boot.CommandLineRunner {

  private final UserRepository userRepository;

  @Override
  public void run(String... args) {
    log.debug("RegisterDefaultUser.run");

    var user = new User();
    user.setUserId(1);

    log.debug("Saving default user {}.", user);
    var savedUser = this.userRepository.save(user);
    log.info("Saved default user {}.", savedUser);

  }
}
