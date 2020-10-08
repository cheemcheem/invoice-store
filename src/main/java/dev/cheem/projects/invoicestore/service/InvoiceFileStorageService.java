package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.InvoiceFileDetailsDTO;
import dev.cheem.projects.invoicestore.exception.StorageException;
import dev.cheem.projects.invoicestore.model.InvoiceFile;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceFileStorageService {

  private final AWSService awsService;

  @Nullable
  public String storeFile(MultipartFile file) {
    log.info("InvoiceFileStorageService.storeFile");
    if (Objects.isNull(file)) {
      return null;
    }

    if (Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
      throw new StorageException("File name is null or empty.");
    }

    if (file.getSize() >= 5000000) {
      log.debug("File size {}", file.getSize());
      throw new StorageException("File is too large (" + file.getSize() + "B, 5MB is the limit).");
    }

    if (file.getSize() == 0 || file.isEmpty()) {
      throw new StorageException("File has size 0B.");
    }

    var fileName = StringUtils.cleanPath(file.getOriginalFilename());

    if (fileName.contains("..")) {
      throw new StorageException("File name contains invalid path sequence: '" + fileName + "' .");
    }

    try {
      return awsService.storeFile(fileName, file.getContentType(), file.getBytes());
    } catch (IOException e) {
      throw new StorageException("Failed to get data from uploaded file.", e);
    }
  }

  public Optional<InvoiceFile> getFile(String invoiceFileId) {
    log.info("InvoiceFileStorageService.getFile");
    log.debug("invoiceFileId = " + invoiceFileId);

    return awsService.getFile(invoiceFileId);

  }

  public void deleteById(String invoiceFileId) {
    awsService.deleteFile(invoiceFileId);
  }
}
