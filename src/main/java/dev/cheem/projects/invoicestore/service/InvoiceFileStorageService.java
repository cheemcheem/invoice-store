package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.exception.StorageException;
import dev.cheem.projects.invoicestore.model.InvoiceFile;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import dev.cheem.projects.invoicestore.repository.InvoiceFileRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceFileStorageService {

  private final InvoiceFileRepository invoiceFileRepository;


  @Nullable
  public Long storeFile(MultipartFile file) {
    log.info("InvoiceFileStorageService.storeFile");
    if (Objects.isNull(file) || file.isEmpty()) {
      return null;
    }

    if (Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
      throw new StorageException("File name is null or empty.");
    }

    var fileName = StringUtils.cleanPath(file.getOriginalFilename());

    if (fileName.contains("..")) {
      throw new StorageException("File name contains invalid path sequence: '" + fileName + "' .");
    }

    var invoiceFile = new InvoiceFile();
    invoiceFile.setFileName(fileName);
    invoiceFile.setFileType(file.getContentType());

    try {
      invoiceFile.setData(BlobProxy.generateProxy(file.getBytes()));
    } catch (IOException e) {
      throw new StorageException("Failed to get data from uploaded file.", e);
    }

    return invoiceFileRepository.save(invoiceFile).getInvoiceFileId();

  }

  @Transactional
  public Optional<InvoiceFile> getFile(String invoiceFileId) {
    log.info("InvoiceFileStorageService.getFile");
    log.debug("invoiceFileId = " + invoiceFileId );

    var invoiceFileIdLong = NumberUtils.parseNumber(invoiceFileId, Long.class);
    var optionalInvoiceFile = invoiceFileRepository.findById(invoiceFileIdLong);

    if (optionalInvoiceFile.isEmpty()) {
      return Optional.empty();
    }

    var invoiceFile = optionalInvoiceFile.get();
    log.debug("invoiceFile = {}", invoiceFile);

    return optionalInvoiceFile;
  }

  public InvoiceFile getDefiniteFile(String invoiceFileId) {
    return getFile(invoiceFileId).get();
  }


}
