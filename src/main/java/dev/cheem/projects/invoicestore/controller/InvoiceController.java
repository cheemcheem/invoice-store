package dev.cheem.projects.invoicestore.controller;

import dev.cheem.projects.invoicestore.exception.StorageException;
import dev.cheem.projects.invoicestore.service.InvoiceDetailsStorageService;
import dev.cheem.projects.invoicestore.service.InvoiceFileStorageService;
import dev.cheem.projects.invoicestore.service.UserService;
import dev.cheem.projects.invoicestore.util.Constants;
import java.time.Duration;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Transactional
  @PutMapping("/file/{invoiceDetailsId}")
  public ResponseEntity<String> uploadInvoiceFile(
      @PathVariable String invoiceDetailsId,
      @RequestParam(name = "invoiceFile", required = false) MultipartFile invoiceFile,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {

    log.info("InvoiceUploadController.uploadInvoice");
    if (!userService.allowedMoreFiles(invoiceUserId)) {
      return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).build();
    }
    try {
      var invoiceFileId = invoiceFileStorageService.storeFile(invoiceFile);
      var attachedFileToInvoice = invoiceDetailsStorageService
          .setInvoiceFileId(invoiceDetailsId, invoiceFileId);

      if (!attachedFileToInvoice) {
        invoiceFileStorageService.deleteById(invoiceFileId);
        return ResponseEntity.notFound().build();
      }

      var invoiceLocationURI = ServletUriComponentsBuilder.fromPath("/view/")
          .path(invoiceDetailsId)
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

  @Transactional
  @SneakyThrows
  @GetMapping("/file/{invoiceDetailsId}")
  public ResponseEntity<Resource> getInvoiceFile(
      @PathVariable String invoiceDetailsId,
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoiceFile");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var invoiceForUser = userService.detailsMatch(invoiceUserId, invoiceDetailsId);

    if (!invoiceForUser) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    var optionalFileId = invoiceDetailsStorageService.getInvoiceFileId(invoiceDetailsId);

    if (optionalFileId.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var optionalFile = invoiceFileStorageService.getFile(optionalFileId.get());

    if (optionalFile.isEmpty()) {
      return ResponseEntity.of(Optional.empty());
    }

    var invoice = optionalFile.get();

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(invoice.getInvoiceFileDetails().getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\""
                + invoice.getInvoiceFileDetails().getFileName()
                + "\""
        )
        .eTag(invoice.getETag())
        .lastModified(invoice.getLastModified())
        .cacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic())
        .body(new ByteArrayResource(
            invoice.getData().getBytes(1, (int) invoice.getData().length()))
        );
  }

  @GetMapping(value = "/csv", produces = "text/csv")
  public ResponseEntity<Resource> getInvoiceCSV(
      @RequestAttribute(Constants.USER_ID_ATTRIBUTE_KEY) Long invoiceUserId
  ) {
    log.info("InvoiceUploadController.getInvoiceList");
    var csvData = invoiceDetailsStorageService.getInvoiceCSV(invoiceUserId);

    if (csvData.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"InvoiceData.csv\"")
        .body(new ByteArrayResource(csvData.get().getBytes()));
  }


}
