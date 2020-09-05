package dev.cheem.projects.invoicestore.controller;

import dev.cheem.projects.invoicestore.dto.InvoiceDetailsDTO;
import dev.cheem.projects.invoicestore.service.InvoiceDetailsStorageService;
import dev.cheem.projects.invoicestore.service.InvoiceFileStorageService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  private final InvoiceFileStorageService invoiceFileStorageService;
  private final InvoiceDetailsStorageService invoiceDetailsStorageService;

  @PostMapping("/new")
  public ResponseEntity<String> uploadInvoice(
      @RequestParam("invoiceDate") @DateTimeFormat(iso = ISO.DATE) Date invoiceDate,
      @RequestParam("invoiceName") String invoiceName,
      @RequestParam("invoiceTotalVAT") BigDecimal invoiceTotalVAT,
      @RequestParam("invoiceTotal") BigDecimal invoiceTotal,
      @RequestParam(name = "invoiceFile", required = false) MultipartFile invoiceFile
  ) {

    log.info("InvoiceUploadController.uploadInvoice");
    var storedInvoiceFile = invoiceFileStorageService.storeFile(invoiceFile);
    var storedInvoice = invoiceDetailsStorageService.storeInvoice(invoiceDate, invoiceName, invoiceTotal, invoiceTotalVAT, storedInvoiceFile);
    var invoiceLocationURI = ServletUriComponentsBuilder.fromPath("/view/")
        .path(storedInvoice.getInvoiceDetailsId().toString())
        .build().toUri();

    return ResponseEntity.status(HttpStatus.SEE_OTHER).location(invoiceLocationURI).build();

  }

  @GetMapping("/details/{invoiceDetailsId}")
  public ResponseEntity<InvoiceDetailsDTO> getInvoice(@PathVariable String invoiceDetailsId) {
    log.info("InvoiceUploadController.getInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);
    var optional = invoiceDetailsStorageService.getInvoiceDetails(invoiceDetailsId);

    return ResponseEntity.of(optional);

  }

  @GetMapping("/file/{invoiceFileId}")
  public ResponseEntity<Resource> getInvoiceFile(@PathVariable String invoiceFileId) {
    log.info("InvoiceUploadController.getInvoiceFile");
    log.debug("invoiceFileId = " + invoiceFileId);
    var optional = invoiceFileStorageService.getFile(invoiceFileId);

    if (optional.isEmpty()) {
      return ResponseEntity.of(Optional.empty());
    }

    var invoice = optional.get();

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(invoice.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + invoice.getFileName() + "\"")
        .body(new ByteArrayResource(invoice.getData()));
  }

  @GetMapping("/all")
  public ResponseEntity<List<String>> getInvoiceList() {
    log.info("InvoiceUploadController.getInvoiceList");
    return ResponseEntity.ok(invoiceDetailsStorageService.getAllInvoiceDetails());
  }

  @GetMapping("/archived")
  public ResponseEntity<List<String>> getArchivedInvoiceList() {
    log.info("InvoiceUploadController.getArchivedInvoiceList");
    return ResponseEntity.ok(invoiceDetailsStorageService.getArchivedInvoiceDetails());
  }

  @PutMapping("/archive/{invoiceDetailsId}")
  public ResponseEntity<String> archiveInvoice(@PathVariable String invoiceDetailsId) {
    log.info("InvoiceController.archiveInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optional = invoiceDetailsStorageService.archiveInvoice(invoiceDetailsId);

    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();

  }

  @PutMapping("/restore/{invoiceDetailsId}")
  public ResponseEntity<String> restoreInvoice(@PathVariable String invoiceDetailsId) {
    log.info("InvoiceController.restoreInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optional = invoiceDetailsStorageService.restoreInvoice(invoiceDetailsId);

    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();

  }

  @DeleteMapping("/delete/{invoiceDetailsId}")
  public ResponseEntity<String> deleteInvoice(@PathVariable String invoiceDetailsId) {
    log.info("InvoiceController.deleteInvoice");
    log.debug("invoiceDetailsId = " + invoiceDetailsId);

    var optionalDeleted = invoiceDetailsStorageService.deleteInvoiceDetails(invoiceDetailsId);

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
