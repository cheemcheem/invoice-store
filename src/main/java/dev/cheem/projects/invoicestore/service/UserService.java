package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.model.AWSInstance;
import dev.cheem.projects.invoicestore.model.DatabaseInstance;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final DatabaseInstance databaseInstance;
  private final AWSInstance awsInstance;
  private final UserRepository userRepository;

  @Transactional
  public User login(@NonNull String name) {
    var oAuthUser = new User();
    log.info("Principal name {}.", name);
    oAuthUser.setOAuth2Id(name);

    var optionalUser = userRepository.findOne(Example.of(oAuthUser));

    return optionalUser.orElseGet(() -> userRepository.save(oAuthUser));
  }

  @Transactional
  public Optional<Long> checkUser(@NonNull Long userId) {
    log.info("UserService.getUser");
    log.debug("userId = " + userId);
    var user = userRepository.findById(userId);

    if (user.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(userId);

  }

  @Transactional
  public User getUser(@NonNull Long userId) {
    log.info("UserService.getUser");
    log.debug("userId = " + userId);
    var user = userRepository.findById(userId);
    return user.orElseThrow();
  }

  @Transactional
  public boolean detailsMatch(@NonNull Long userId, @NonNull String invoiceDetailsId) {
    return userRepository.getOne(userId)
        .getInvoiceDetailsSet().stream()
        .map(InvoiceDetails::getInvoiceDetailsId)
        .filter(Objects::nonNull)
        .anyMatch(invoiceDetailsId::equals);
  }

  public boolean allowedMoreFiles(@NonNull Long userId) {
    return userRepository.getOne(userId)
        .getInvoiceDetailsSet().stream()
        .map(InvoiceDetails::getInvoiceDetailsId)
        .filter(Objects::nonNull)
        .count() < awsInstance.getMaxFileLimit();
  }

  public boolean allowedMoreUsers() {
    return userRepository.count() < databaseInstance.getMaxUserLimit();
  }
}
