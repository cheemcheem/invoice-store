package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.UserDTO;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserDTO login(String name) {
    var oAuthUser = new User();
    log.info("Principal name {}.", name);
    oAuthUser.setOAuth2Id(name);

    var optionalUser = userRepository.findOne(Example.of(oAuthUser));

    var user = optionalUser.orElseGet(() -> userRepository.save(oAuthUser));

    return UserDTO.builder().userId(user.getUserId().toString()).build();
  }

  @Transactional
  public Optional<Long> checkUser(Long userId) {
    log.info("UserService.getUser");
    log.debug("userId = " + userId);
    var user = userRepository.findById(userId);

    if (user.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(userId);

  }

  @Transactional
  public User getUser(Long userId) {
    log.info("UserService.getUser");
    log.debug("userId = " + userId);
    var user = userRepository.findById(userId);
    return user.orElseThrow();
  }


  @Transactional
  public boolean detailsMatch(Long userId, Long invoiceDetailsId) {
    return userRepository.getOne(userId)
        .getInvoiceDetailsSet().stream()
        .map(InvoiceDetails::getInvoiceDetailsId)
        .filter(Objects::nonNull)
        .anyMatch(invoiceDetailsId::equals);
  }
}
