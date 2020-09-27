package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.config.AWSConfig.AWSInstance;
import dev.cheem.projects.invoicestore.dto.InvoiceFileDetailsDTO;
import dev.cheem.projects.invoicestore.exception.StorageException;
import dev.cheem.projects.invoicestore.model.InvoiceFile;
import dev.cheem.projects.invoicestore.model.InvoiceFileDetails;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AWSService {

  private final AWSInstance awsInstance;

  public Optional<InvoiceFile> getFile(String uuid) {
    var key = getKey(uuid);
    var getRequest = awsInstance.getGetS3ObjectRequestBuilder()
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> object;
    try {
      object = awsInstance.getS3Client().getObject(getRequest);
    } catch (NoSuchKeyException e) {
      log.warn("Key not found, key = '{}'", key, e);
      return Optional.empty();
    }

    byte[] bytes;
    try {
      bytes = object.readAllBytes();
    } catch (IOException e) {
      throw new StorageException("Failed to read bytes from file '" + key + "'.", e);
    }

    var fileType = object.response().contentType();
    var fileName = object.response().metadata().get(awsInstance.getS3FileNameMetaTag());

    var invoiceFileDetails = new InvoiceFileDetails();
    invoiceFileDetails.setFileName(fileName);
    invoiceFileDetails.setFileType(fileType);

    var invoiceFile = new InvoiceFile();
    invoiceFile.setInvoiceFileDetails(invoiceFileDetails);
    invoiceFile.setData(BlobProxy.generateProxy(bytes));
    invoiceFile.setETag(object.response().eTag());
    invoiceFile.setLastModified(object.response().lastModified());
    return Optional.of(invoiceFile);
  }

  public Optional<InvoiceFileDetailsDTO> getFileDetails(String uuid) {
    var key = getKey(uuid);
    var getRequest = awsInstance.getGetS3ObjectRequestBuilder()
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> object;
    try {
      object = awsInstance.getS3Client().getObject(getRequest);
    } catch (NoSuchKeyException e) {
      log.warn("Key not found, key = '{}'", key, e);
      return Optional.empty();
    }

    var fileType = object.response().contentType();
    System.out.println("object.response().metadata() = " + object.response().metadata());
    System.out.println(
        "object.response().metadata().get(awsInstance.getS3FileNameMetaTag()) = " + object
            .response().metadata().get(awsInstance.getS3FileNameMetaTag()));
    System.out.println(
        "object.response().getValueForField(awsInstance.getS3FileNameMetaTag(), String.class) = "
            + object.response().getValueForField(awsInstance.getS3FileNameMetaTag(), String.class));
    var fileName = object.response().metadata().get(awsInstance.getS3FileNameMetaTag());

    var invoiceFile = new InvoiceFileDetailsDTO();
    invoiceFile.setInvoiceFileName(fileName);
    invoiceFile.setInvoiceFileType(fileType);
    return Optional.of(invoiceFile);
  }

  private String getKey(String uuid) {
    return awsInstance.getS3RootDirectory() + "/" + uuid;
  }

  public void deleteFile(String uuid) {
    var key = getKey(uuid);
    var deleteRequest = awsInstance.getDeleteS3ObjectRequestBuilder()
        .key(key)
        .build();
    awsInstance.getS3Client().deleteObject(deleteRequest);
  }

  public String storeFile(String fileName, String fileType, byte[] fileBytes) {
    var uuid = UUID.randomUUID().toString();
    var key = getKey(uuid);
    var putRequest = awsInstance.getPutS3ObjectRequestBuilder()
        .metadata(Map.of("x-amz-meta-" + awsInstance.getS3FileNameMetaTag(), fileName))
        .contentType(fileType)
        .key(key)
        .build();
    awsInstance.getS3Client().putObject(putRequest, RequestBody.fromBytes(fileBytes));
    return uuid;
  }
}
