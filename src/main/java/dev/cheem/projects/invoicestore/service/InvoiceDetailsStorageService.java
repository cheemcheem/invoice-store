package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.InvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.dto.InvoiceFileDTO;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.InvoiceFile;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceDetailsStorageService {

  private final InvoiceDetailsRepository invoiceDetailsRepository;

  public InvoiceDetails storeInvoice(Date invoiceDate, String invoiceName,
      BigDecimal invoiceTotalVAT, BigDecimal invoiceTotal, @Nullable InvoiceFile invoiceFile,
      User invoiceUser) {
    log.info("InvoiceDetailsStorageService.storeInvoice");

    var invoice = new InvoiceDetails();
    invoice.setInvoiceDate(invoiceDate);
    invoice.setInvoiceName(invoiceName);
    invoice.setInvoiceTotalVAT(invoiceTotalVAT);
    invoice.setInvoiceTotal(invoiceTotal);
    invoice.setInvoiceFile(invoiceFile);
    invoice.setInvoiceArchived(false);
    invoice.setInvoiceUser(invoiceUser);

    return invoiceDetailsRepository.save(invoice);
  }

  @Transactional
  public Optional<InvoiceDetailsDTO> getInvoiceDetails(String invoiceDetailsId, User invoiceUser) {
    log.info("InvoiceDetailsStorageService.getInvoiceDetails");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceUser = " + invoiceUser + ", invoiceUser = " + invoiceUser);
    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optionalDetails = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optionalDetails.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optionalDetails.get();

    if (invoiceDetails.getInvoiceUser().getUserId() != invoiceUser.getUserId()) {
      return Optional.empty();
    }

    var invoiceDetailsDTO = new InvoiceDetailsDTO();

    invoiceDetailsDTO.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
    invoiceDetailsDTO.setInvoiceDate(invoiceDetails.getInvoiceDate());
    invoiceDetailsDTO.setInvoiceName(invoiceDetails.getInvoiceName());
    invoiceDetailsDTO.setInvoiceTotalVAT(invoiceDetails.getInvoiceTotalVAT());
    invoiceDetailsDTO.setInvoiceTotal(invoiceDetails.getInvoiceTotal());
    invoiceDetailsDTO.setInvoiceArchived(invoiceDetails.getInvoiceArchived());

    if (Objects.isNull(invoiceDetails.getInvoiceFile())) {
      return Optional.of(invoiceDetailsDTO);
    }

    var invoiceFileDTO = new InvoiceFileDTO();
    invoiceFileDTO.setInvoiceFileId(invoiceDetails.getInvoiceFile().getInvoiceFileId().toString());
    invoiceFileDTO.setInvoiceFileType(invoiceDetails.getInvoiceFile().getFileType());
    invoiceFileDTO.setInvoiceFileName(invoiceDetails.getInvoiceFile().getFileName());

    invoiceDetailsDTO.setInvoiceFile(invoiceFileDTO);

    return Optional.of(invoiceDetailsDTO);

  }

  public List<String> getAllInvoiceDetails(User invoiceUser) {
    return invoiceDetailsRepository.findAll().stream()
        .filter(i -> i.getInvoiceUser().getUserId() == invoiceUser.getUserId())
        .filter(Predicate.not(InvoiceDetails::getInvoiceArchived))
        .map(InvoiceDetails::getInvoiceDetailsId)
        .map(Objects::toString)
        .collect(Collectors.toList());
  }

  public List<String> getArchivedInvoiceDetails(User invoiceUser) {
    return invoiceDetailsRepository.findAll().stream()
        .filter(i -> i.getInvoiceUser().getUserId() == invoiceUser.getUserId())
        .filter(InvoiceDetails::getInvoiceArchived)
        .map(InvoiceDetails::getInvoiceDetailsId)
        .map(Objects::toString)
        .collect(Collectors.toList());
  }

  public Optional<Boolean> deleteInvoiceDetails(String invoiceDetailsId, User invoiceUser) {
    log.info("InvoiceDetailsStorageService.deleteInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceUser = " + invoiceUser);

    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optional = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optional.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optional.get();

    if (invoiceDetails.getInvoiceUser().getUserId() != invoiceUser.getUserId()) {
      return Optional.of(false);
    }

    if (!invoiceDetails.getInvoiceArchived()) {
      return Optional.of(false);
    }

    invoiceDetailsRepository.deleteById(invoiceDetails.getInvoiceDetailsId());
    return Optional.of(true);

  }

  public Optional<InvoiceDetails> archiveInvoice(String invoiceDetailsId, User invoiceUser) {
    return changeArchiveStatus(invoiceDetailsId, true, invoiceUser);
  }

  public Optional<InvoiceDetails> restoreInvoice(String invoiceDetailsId, User invoiceUser) {
    return changeArchiveStatus(invoiceDetailsId, false, invoiceUser);
  }

  private Optional<InvoiceDetails> changeArchiveStatus(String invoiceDetailsId,
      boolean invoiceArchived, User invoiceUser) {
    log.info("InvoiceDetailsStorageService.changeArchiveStatus");
    log.debug("invoiceDetailsId = " + invoiceDetailsId + ", invoiceArchived = " + invoiceArchived
        + ", invoiceUser = " + invoiceUser);

    var invoiceDetailsIdLong = NumberUtils.parseNumber(invoiceDetailsId, Long.class);
    var optionalDetails = invoiceDetailsRepository.findById(invoiceDetailsIdLong);

    if (optionalDetails.isEmpty()) {
      return Optional.empty();
    }

    var invoiceDetails = optionalDetails.get();

    if (invoiceDetails.getInvoiceUser().getUserId() != invoiceUser.getUserId()) {
      return Optional.empty();
    }

    invoiceDetails.setInvoiceArchived(invoiceArchived);

    return Optional.of(invoiceDetailsRepository.save(invoiceDetails));
  }
}
