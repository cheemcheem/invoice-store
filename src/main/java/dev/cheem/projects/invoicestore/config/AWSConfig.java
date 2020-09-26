package dev.cheem.projects.invoicestore.config;

import java.net.URI;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Setter
@Configuration
@Slf4j
public class AWSConfig {

  @Value("${AWS_S3_BUCKET}")
  private String bucket;

  @Value("${AWS_REGION}")
  private String region;

  @Value("${AWS_ENDPOINT}")
  private String endpoint;

  @Value("${AWS_S3_BUCKET_ROOT}")
  private String root;

  private S3Client S3Client() {
    log.info("Creating S3 Client");
    var s3Client = S3Client.builder()
        .region(Region.of(region))
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();
    log.debug("S3Client '{}' ", s3Client);
    return s3Client;
  }

  private GetObjectRequest.Builder getS3ObjectRequestBuilder() {
    return GetObjectRequest.builder().bucket(bucket);
  }

  private DeleteObjectRequest.Builder deleteS3ObjectRequestBuilder() {
    return DeleteObjectRequest.builder().bucket(bucket);
  }

  private PutObjectRequest.Builder putS3ObjectRequestBuilder() {
    return PutObjectRequest.builder().bucket(bucket);
  }

  private String s3RootDirectory() {
    return root;
  }

  private String s3FileNameMetaTag() {
    return "original-file-name";
  }

  @Bean
  public AWSInstance awsInstance() {
    return new AWSInstance(
        S3Client(),
        getS3ObjectRequestBuilder(),
        deleteS3ObjectRequestBuilder(),
        putS3ObjectRequestBuilder(),
        s3RootDirectory(),
        s3FileNameMetaTag()
    );
  }

  @Data
  public static class AWSInstance {
    private final S3Client S3Client;
    private final GetObjectRequest.Builder getS3ObjectRequestBuilder;
    private final DeleteObjectRequest.Builder deleteS3ObjectRequestBuilder;
    private final PutObjectRequest.Builder putS3ObjectRequestBuilder;
    private final String s3RootDirectory;
    private final String s3FileNameMetaTag;
  }
}
