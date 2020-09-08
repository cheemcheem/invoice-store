package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.InvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.dto.InvoiceFileDTO;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.InvoiceFile;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import dev.cheem.projects.invoicestore.repository.InvoiceFileRepository;
import dev.cheem.projects.invoicestore.util.LocalDateTimeConverter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceDetailsStorageService {

  private final UserService userService;
  private final InvoiceFileStorageService invoiceFileStorageService;
  private final InvoiceDetailsRepository invoiceDetailsRepository;

  @Transactional
  public InvoiceDetails storeInvoice(Date invoiceDate, String invoiceName,
      BigDecimal invoiceTotalVAT, BigDecimal invoiceTotal, @Nullable Long invoiceFileId,
      Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.storeInvoice");

    var invoice = new InvoiceDetails();
    invoice.setInvoiceDate(invoiceDate);
    invoice.setInvoiceName(invoiceName);
    invoice.setInvoiceTotalVAT(invoiceTotalVAT);
    invoice.setInvoiceTotal(invoiceTotal);
    invoice.setInvoiceFile(invoiceFileStorageService.getDefiniteFile(String.valueOf(invoiceFileId)));
    invoice.setInvoiceArchived(false);
    invoice.setInvoiceUser(userService.getUser(invoiceUserId));

    return invoiceDetailsRepository.save(invoice);
  }

  public Optional<InvoiceDetailsDTO> getInvoiceDetails(String invoiceDetailsId, Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.getInvoiceDetails");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceUserId = " + invoiceUserId);
    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optionalDetails = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optionalDetails.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optionalDetails.get();
    Hibernate.initialize(invoiceDetails.getInvoiceUser().getUserId());
    if (!Objects.equals(invoiceDetails.getInvoiceUser().getUserId(), invoiceUserId)) {
      return Optional.empty();
    }
    var invoiceDetailsDTO = new InvoiceDetailsDTO();

    Hibernate.initialize(invoiceDetails.getInvoiceDetailsId());
    invoiceDetailsDTO.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
    Hibernate.initialize(invoiceDetails.getInvoiceDate());
    invoiceDetailsDTO.setInvoiceDate(LocalDateTimeConverter.formatISO(invoiceDetails.getInvoiceDate()));
    Hibernate.initialize(invoiceDetails.getInvoiceName());
    invoiceDetailsDTO.setInvoiceName(invoiceDetails.getInvoiceName());
    Hibernate.initialize(invoiceDetails.getInvoiceTotalVAT());
    invoiceDetailsDTO.setInvoiceTotalVAT(invoiceDetails.getInvoiceTotalVAT());
    Hibernate.initialize(invoiceDetails.getInvoiceTotal());
    invoiceDetailsDTO.setInvoiceTotal(invoiceDetails.getInvoiceTotal());
    Hibernate.initialize(invoiceDetails.getInvoiceArchived());
    invoiceDetailsDTO.setInvoiceArchived(invoiceDetails.getInvoiceArchived());

    Hibernate.initialize(invoiceDetails.getInvoiceFile());
    if (Objects.isNull(invoiceDetails.getInvoiceFile())) {
      return Optional.of(invoiceDetailsDTO);
    }

    Hibernate.unproxy(invoiceDetails.getInvoiceFile().getData());
    
    var invoiceFileDTO = new InvoiceFileDTO();
    Hibernate.initialize(invoiceDetails.getInvoiceFile().getInvoiceFileId());
    invoiceFileDTO.setInvoiceFileId(invoiceDetails.getInvoiceFile().getInvoiceFileId().toString());
    Hibernate.initialize(invoiceDetails.getInvoiceFile().getFileType());
    invoiceFileDTO.setInvoiceFileType(invoiceDetails.getInvoiceFile().getFileType());
    Hibernate.initialize(invoiceDetails.getInvoiceFile().getFileName());
    invoiceFileDTO.setInvoiceFileName(invoiceDetails.getInvoiceFile().getFileName());

    invoiceDetailsDTO.setInvoiceFile(invoiceFileDTO);

    return Optional.of(invoiceDetailsDTO);

  }

  public List<String> getAllInvoiceDetails(Long invoiceUserId) {
    return invoiceDetailsRepository.findAll().stream()
        .filter(i -> Objects.equals(i.getInvoiceUser().getUserId(), invoiceUserId))
        .filter(Predicate.not(InvoiceDetails::getInvoiceArchived))
        .map(InvoiceDetails::getInvoiceDetailsId)
        .map(Objects::toString)
        .collect(Collectors.toList());
  }

  public List<String> getArchivedInvoiceDetails(Long invoiceUserId) {
    return invoiceDetailsRepository.findAll().stream()
        .filter(i -> Objects.equals(i.getInvoiceUser().getUserId(), invoiceUserId))
        .filter(InvoiceDetails::getInvoiceArchived)
        .map(InvoiceDetails::getInvoiceDetailsId)
        .map(Objects::toString)
        .collect(Collectors.toList());
  }

  public Optional<Boolean> deleteInvoiceDetails(String invoiceDetailsId, Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.deleteInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceUserId = " + invoiceUserId);

    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optional = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optional.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optional.get();

    if (!Objects.equals(invoiceDetails.getInvoiceUser().getUserId(), invoiceUserId)) {
      return Optional.of(false);
    }

    if (!invoiceDetails.getInvoiceArchived()) {
      return Optional.of(false);
    }

    invoiceDetailsRepository.deleteById(invoiceDetails.getInvoiceDetailsId());
    return Optional.of(true);

  }

  public Optional<InvoiceDetails> archiveInvoice(String invoiceDetailsId, Long invoiceUserId) {
    return changeArchiveStatus(invoiceDetailsId, true, invoiceUserId);
  }

  public Optional<InvoiceDetails> restoreInvoice(String invoiceDetailsId, Long invoiceUserId) {
    return changeArchiveStatus(invoiceDetailsId, false, invoiceUserId);
  }

  private Optional<InvoiceDetails> changeArchiveStatus(String invoiceDetailsId,
      boolean invoiceArchived, Long invoiceUserId) {
    log.info("InvoiceDetailsStorageService.changeArchiveStatus");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceArchived = " + invoiceArchived
        + ", invoiceUserId = " + invoiceUserId);

    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optionalDetails = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optionalDetails.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optionalDetails.get();

    if (!Objects.equals(invoiceDetails.getInvoiceUser().getUserId(), invoiceUserId)) {
      return Optional.empty();
    }

    invoiceDetails.setInvoiceArchived(invoiceArchived);

    return Optional.of(invoiceDetailsRepository.save(invoiceDetails));
  }
}
