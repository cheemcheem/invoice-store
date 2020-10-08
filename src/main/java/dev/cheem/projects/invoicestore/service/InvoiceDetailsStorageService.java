package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.InvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import dev.cheem.projects.invoicestore.util.LocalDateTimeConverter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceDetailsStorageService {

  @PersistenceContext
  private final EntityManager entityManager;

  private final UserService userService;
  private final InvoiceFileStorageService invoiceFileStorageService;
  private final InvoiceDetailsRepository invoiceDetailsRepository;

  @Transactional
  public InvoiceDetails storeInvoice(Date invoiceDate, String invoiceName,
      BigDecimal invoiceTotalVAT, BigDecimal invoiceTotal, @Nullable String invoiceFileId,
      Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.storeInvoice");

    var invoice = new InvoiceDetails();
    invoice.setInvoiceDate(invoiceDate);
    invoice.setInvoiceName(invoiceName);
    invoice.setInvoiceTotalVAT(invoiceTotalVAT);
    invoice.setInvoiceTotal(invoiceTotal);
    if (Objects.nonNull(invoiceFileId)) {
      invoice.setInvoiceFile(invoiceFileId);
    }
    invoice.setInvoiceArchived(false);
    invoice.setInvoiceUser(userService.getUser(invoiceUserId));

    return invoiceDetailsRepository.save(invoice);
  }

  @Transactional
  public Optional<InvoiceDetailsDTO> getInvoiceDetails(String invoiceDetailsId,
      Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.getInvoiceDetails");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceUserId = " + invoiceUserId);
    var optionalDetails = invoiceDetailsRepository.findById(invoiceDetailsId);

    if (optionalDetails.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optionalDetails.get();

    if (!Objects.equals(invoiceDetails.getInvoiceUser().getUserId(), invoiceUserId)) {
      return Optional.empty();
    }
    var invoiceDetailsDTO = new InvoiceDetailsDTO();

    invoiceDetailsDTO.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
    invoiceDetailsDTO
        .setInvoiceDate(LocalDateTimeConverter.formatISO(invoiceDetails.getInvoiceDate()));
    invoiceDetailsDTO.setInvoiceName(invoiceDetails.getInvoiceName());
    invoiceDetailsDTO.setInvoiceTotalVAT(invoiceDetails.getInvoiceTotalVAT());
    invoiceDetailsDTO.setInvoiceTotal(invoiceDetails.getInvoiceTotal());
    invoiceDetailsDTO.setInvoiceArchived(invoiceDetails.getInvoiceArchived());

    if (Objects.isNull(invoiceDetails.getInvoiceFile())) {
      return Optional.of(invoiceDetailsDTO);
    }

    var invoiceFileDTO = invoiceFileStorageService
        .getDefiniteFileDetails(invoiceDetails.getInvoiceFile());

    invoiceDetailsDTO.setInvoiceFile(invoiceFileDTO);

    return Optional.of(invoiceDetailsDTO);

  }

  public Optional<String> getInvoiceCSV(Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.getInvoiceCSV");
    log.debug("invoiceUserId = " + invoiceUserId);

    var csvBuilder = new StringBuilder();

    invoiceDetailsRepository.findAll().stream()
        .filter(i -> Objects.equals(i.getInvoiceUser().getUserId(), invoiceUserId))
        .filter(Predicate.not(InvoiceDetails::getInvoiceArchived))
        .sorted(Comparator.comparingLong(value -> value.getInvoiceDate().getTime()))
        .forEachOrdered(invoiceDetails -> csvBuilder.append('\r').append('\n')
            .append(invoiceDetails.getInvoiceDetailsId()).append(',')
            .append(invoiceDetails.getInvoiceDate()).append(',')
            .append(invoiceDetails.getInvoiceName()).append(',')
            .append(invoiceDetails.getInvoiceTotalVAT()).append(',')
            .append(invoiceDetails.getInvoiceTotal()));

    if (csvBuilder.length() == 0) {
      return Optional.empty();
    }

    var headers = "Invoice Id, Invoice Date, Invoice Name, Invoice Total VAT, Invoice Total";
    return Optional.of(csvBuilder.insert(0, headers).toString());

  }

  public Optional<String> getInvoiceFileId(String invoiceDetailsId) {
    var invoiceDetails = invoiceDetailsRepository.findById(invoiceDetailsId);
    if (invoiceDetails.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(invoiceDetails.get().getInvoiceFile());
  }
}
