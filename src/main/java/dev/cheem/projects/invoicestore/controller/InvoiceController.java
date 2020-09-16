package dev.cheem.projects.invoicestore.controller;

import dev.cheem.projects.invoicestore.dto.BasicInvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.dto.InvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.exception.StorageException;
import dev.cheem.projects.invoicestore.service.InvoiceDetailsStorageService;
import dev.cheem.projects.invoicestore.service.InvoiceFileStorageService;
import dev.cheem.projects.invoicestore.service.UserService;
import dev.cheem.projects.invoicestore.util.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/invoice")
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

  private final UserService userService;
  private final InvoiceFileStorageService invoiceFileStorageService;
  private final InvoiceDetailsStorageService invoiceDetailsStorageService;

  @PostMapping("/new")
  public ResponseEntity<String> uploadInvoice(
      @RequestParam("invoiceDate") @DateTimeFormat(iso = ISO.DATE) Date invoiceDate,
      @RequestParam("invoiceName") String invoiceName,
      @RequestParam("invoiceTotalVAT") BigDecimal invoiceTotalVAT,
      @RequestParam("invoiceTotal") BigDecimal invoiceTotal,
      @RequestParam(name = "invoiceFile", required = false) MultipartFile invoiceFile,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {

    log.info("InvoiceUploadController.uploadInvoice");
    try {
      var storedInvoiceFileId = invoiceFileStorageService.storeFile(invoiceFile);
      var storedInvoice = invoiceDetailsStorageService
          .storeInvoice(invoiceDate, invoiceName, invoiceTotalVAT, invoiceTotal,
              storedInvoiceFileId,
              invoiceUserId);
      var invoiceLocationURI = ServletUriComponentsBuilder.fromPath("/view/")
          .path(storedInvoice.getInvoiceDetailsId().toString())
          .build().toUri();

      return ResponseEntity.status(HttpStatus.SEE_OTHER).location(invoiceLocationURI).build();
    } catch (StorageException e) {
      if (e.getMessage().startsWith("File is too large")) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
      }
    } catch (Exception e) {
      log.error("Unhandled exception creating new invoice.", e);
      return ResponseEntity.badRequest().build();
    }

    log.error("Failed to build a previous response.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @GetMapping("/details/{invoiceDetailsId}")
  public ResponseEntity<InvoiceDetailsDTO> getInvoice(
      @PathVariable String invoiceDetailsId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);
    var optional = invoiceDetailsStorageService.getInvoiceDetails(invoiceDetailsId, invoiceUserId);

    return ResponseEntity.of(optional);

  }

  @Transactional
  @SneakyThrows
  @GetMapping("/file/{invoiceFileId}")
  public ResponseEntity<Resource> getInvoiceFile(
      @PathVariable String invoiceFileId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoiceFile");
    log.debug("invoiceFileId = " + invoiceFileId);
    var invoiceFileIdLong = NumberUtils.parseNumber(invoiceFileId, Long.class);

    var invoiceForUser = userService.fileMatches(invoiceUserId, invoiceFileIdLong);

    if (!invoiceForUser) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    var optional = invoiceFileStorageService.getFile(invoiceFileId);

    if (optional.isEmpty()) {
      return ResponseEntity.of(Optional.empty());
    }

    var invoice = optional.get();

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(invoice.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + invoice.getFileName() + "\"")
        .body(
            new ByteArrayResource(invoice.getData().getBytes(1, (int) invoice.getData().length())));
  }

  @GetMapping("/csv")
  public ResponseEntity<Resource> getInvoiceCSV(
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoiceList");
    var csvData = invoiceDetailsStorageService.getInvoiceCSV(invoiceUserId);

    if (csvData.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"InvoiceData.csv\"")
        .body(new ByteArrayResource(csvData.get().getBytes()));
  }

  @GetMapping("/all")
  public ResponseEntity<List<BasicInvoiceDetailsDTO>> getInvoiceList(
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoiceList");
    return ResponseEntity.ok(invoiceDetailsStorageService.getAllInvoiceDetails(invoiceUserId));
  }

  @GetMapping("/archived")
  public ResponseEntity<List<BasicInvoiceDetailsDTO>> getArchivedInvoiceList(
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getArchivedInvoiceList");
    return ResponseEntity.ok(invoiceDetailsStorageService.getArchivedInvoiceDetails(invoiceUserId));
  }

  @PutMapping("/archive/{invoiceDetailsId}")
  public ResponseEntity<String> archiveInvoice(
      @PathVariable String invoiceDetailsId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceController.archiveInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optional = invoiceDetailsStorageService.archiveInvoice(invoiceDetailsId, invoiceUserId);

    if (!optional) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();

  }

  @PutMapping("/restore/{invoiceDetailsId}")
  public ResponseEntity<String> restoreInvoice(
      @PathVariable String invoiceDetailsId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceController.restoreInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optional = invoiceDetailsStorageService.restoreInvoice(invoiceDetailsId, invoiceUserId);

    if (!optional) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();

  }

  @DeleteMapping("/delete/{invoiceDetailsId}")
  public ResponseEntity<String> deleteInvoice(
      @PathVariable String invoiceDetailsId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceController.deleteInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optionalDeleted = invoiceDetailsStorageService
        .deleteInvoiceDetails(invoiceDetailsId, invoiceUserId);

    if (optionalDeleted.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var deletedDetails = optionalDeleted.get();

    if (deletedDetails) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

  }

}
