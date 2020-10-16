package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.model.AWSInstance;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLUserDTO;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import dev.cheem.projects.invoicestore.util.Constants;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Slf4j
@Service
public class GraphQLUserService {

  private final AWSInstance awsInstance;
  private final UserRepository userRepository;
  private final GraphQLMappingService graphQLMappingService;

  public Long getUserId() {
    log.debug("GraphQLUserService.getUserId");
    var attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return NumberUtils.parseNumber((String) Objects.requireNonNull(
        attr.getAttribute(Constants.USER_ID_SESSION_KEY, RequestAttributes.SCOPE_SESSION)
    ), Long.class);
  }

  public String getName() {
    log.debug("GraphQLUserService.getUserName");
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      return ((OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("name");
    }
    return null;
  }

  public String getPictureURL() {
    log.debug("GraphQLUserService.getPictureURL");
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      return ((OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("picture");
    }
    return null;
  }

  public boolean allowedMoreFiles(Long userId) {
    return userRepository.getOne(userId)
        .getInvoiceDetailsSet().stream()
        .map(InvoiceDetails::getInvoiceDetailsId)
        .filter(Objects::nonNull)
        .count() < awsInstance.getMaxFileLimit();
  }

  public GraphQLUserDTO getUser(Set<String> fields) {
    log.debug("GraphQLUserService.getUser");
    return graphQLMappingService.mapUserFields(
        this::getUserId,
        this::getName,
        this::getPictureURL,
        fields
    ).get();
  }
}
