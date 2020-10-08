package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.config.AWSConfig.AWSInstance;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceFileDTO;
import dev.cheem.projects.invoicestore.repository.InvoiceDetailsRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@RequiredArgsConstructor
@Slf4j
@Service
public class GraphQLInvoiceFileService {

  private final AWSInstance awsInstance;
  private final InvoiceDetailsRepository invoiceDetailsRepository;
  private final GraphQLMappingService graphQLMappingService;

  public Optional<GraphQLInvoiceFileDTO> getInvoiceFileByInvoiceId(
      Long userId,
      String invoiceId,
      Set<String> fields
  ) {
    log.debug("GraphQLInvoiceFileService.getInvoiceFileByInvoiceId");

    var optional = invoiceDetailsRepository.findById(invoiceId);
    if (optional.isEmpty()) {
      return Optional.empty();
    }

    if (!optional.get().getInvoiceUser().getUserId().equals(userId)) {
      return Optional.empty();
    }

    var uuid = optional.get().getInvoiceFile();

    if (uuid == null) {
      log.debug("Invoice file key is null for invoice {}.", invoiceId);
      return Optional.empty();
    }

    var key = getKey(uuid);
    var getRequest = awsInstance.getGetS3ObjectRequestBuilder()
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> object;
    try {
      object = awsInstance.getS3Client().getObject(getRequest);
    } catch (NoSuchKeyException e) {
      log.warn("Key not found, key = '{}'", key);
      return Optional.empty();
    }

    return Optional.of(
        graphQLMappingService.mapFileFields(
            fields,
            () -> object.response().metadata().get(awsInstance.getS3FileNameMetaTag()),
            () -> object.response().contentType()
        ).get()
    );
  }

  private String getKey(String uuid) {
    return awsInstance.getS3RootDirectory() + "/" + uuid;
  }
}
