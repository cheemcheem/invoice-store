package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.UserDTO;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserDTO login(String name) {
    var oAuthUser = new User();
    log.info("Principal name {}.", name);
    oAuthUser.setOAuth2Id(name);

    var optionalUser = userRepository.findOne(Example.of(oAuthUser));

    var user = optionalUser.orElseGet(() -> userRepository.save(oAuthUser));

    return UserDTO.builder().userId(user.getUserId().toString()).build();

  }


  public UserDTO getUser(String name) {
    return UserDTO.builder().userId(name).build();
  }

}
