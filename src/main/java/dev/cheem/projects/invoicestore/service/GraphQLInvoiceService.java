package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceDTO;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GraphQLInvoiceService {

  private final GraphQLMappingService graphQLMappingService;
  private final InvoiceDetailsRepository invoiceDetailsRepository;
  private final UserRepository userRepository;

  @Transactional
  public List<GraphQLInvoiceDTO> getAllInvoiceDetailsGQL(Long userId, Set<String> fields) {
    log.debug("GraphQLInvoiceService.getAllInvoiceDetailsGQL");
    return userRepository.findById(userId).orElseThrow().getInvoiceDetailsSet()
        .stream()
        .map(graphQLMappingService.mapInvoiceFields(fields))
        .collect(Collectors.toList());
  }

  public Optional<GraphQLInvoiceDTO> getInvoiceById(
      Long userId,
      String invoiceId,
      Set<String> fields
  ) {
    log.debug("GraphQLInvoiceService.getInvoiceById");
    var optional = invoiceDetailsRepository.findById(invoiceId);
    if (optional.isEmpty()) {
      return Optional.empty();
    }

    if (!optional.get().getInvoiceUser().getUserId().equals(userId)) {
      return Optional.empty();
    }

    return Optional.of(
        graphQLMappingService.mapInvoiceFields(fields).apply(optional.get())
    );
  }

  public Optional<GraphQLInvoiceDTO> createInvoice(
      Long userId,
      Map<String, Object> input,
      Set<String> fields
  ) {
    log.debug("GraphQLInvoiceService.createInvoice");
    var invoice = graphQLMappingService.mapInvoiceInputFields(
        () -> userRepository.getOne(userId)
    ).apply(input);
    var saved = invoiceDetailsRepository.save(invoice);

    return Optional.of(
        graphQLMappingService.mapInvoiceFields(fields).apply(saved)
    );
  }

  @Transactional
  public Optional<GraphQLInvoiceDTO> updateInvoice(
      Long userId,
      Map<String, Object> input,
      Set<String> fields
  ) {
    log.debug("GraphQLInvoiceService.updateInvoice");
    var updatedInvoice = graphQLMappingService.mapUpdateInputFields().apply(input);

    var optionalExistingInvoice = invoiceDetailsRepository
        .findById(updatedInvoice.getInvoiceDetailsId());
    if (optionalExistingInvoice.isEmpty()) {
      return Optional.empty();
    }

    var existingInvoice = optionalExistingInvoice.get();
    if (!existingInvoice.getInvoiceUser().getUserId().equals(userId)) {
      return Optional.empty();
    }

    graphQLMappingService.updateInvoice(existingInvoice, updatedInvoice);

    var saved = invoiceDetailsRepository.save(existingInvoice);

    return Optional.of(
        graphQLMappingService.mapInvoiceFields(fields).apply(saved)
    );
  }

  public Boolean deleteInvoice(Long userId, String id) {
    log.debug("GraphQLInvoiceService.deleteInvoice");
    var optionalInvoice = invoiceDetailsRepository.findById(id);
    if (optionalInvoice.isEmpty()) {
      return false;
    }

    var invoice = optionalInvoice.get();
    if (!invoice.getInvoiceUser().getUserId().equals(userId)) {
      return false;
    }

    invoiceDetailsRepository.delete(invoice);
    return true;
  }
}
