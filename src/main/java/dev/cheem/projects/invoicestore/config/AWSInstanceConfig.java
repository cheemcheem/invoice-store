package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.model.AWSInstance;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@DependsOn("awsConfig")
@RequiredArgsConstructor
@Configuration
@Slf4j
public class AWSInstanceConfig {

  private final String awsS3Bucket;
  private final String awsRegion;
  private final String awsEndpoint;
  private final String awsS3BucketRoot;
  private final String awsMaxFileLimit;
  private final String awsAccessKeyId;
  private final String awsSecretAccessKey;

  private S3Client S3Client() {
    return S3Client.builder()
        .region(Region.of(awsRegion))
        .endpointOverride(URI.create(awsEndpoint))
        .credentialsProvider(() -> new AwsCredentials() {
          @Override
          public String accessKeyId() {
            return awsAccessKeyId;
          }

          @Override
          public String secretAccessKey() {
            return awsSecretAccessKey;
          }
        })
        .build();
  }

  private GetObjectRequest.Builder getS3ObjectRequestBuilder() {
    return GetObjectRequest.builder().bucket(awsS3Bucket);
  }

  private DeleteObjectRequest.Builder deleteS3ObjectRequestBuilder() {
    return DeleteObjectRequest.builder().bucket(awsS3Bucket);
  }

  private PutObjectRequest.Builder putS3ObjectRequestBuilder() {
    return PutObjectRequest.builder().bucket(awsS3Bucket);
  }

  private String s3RootDirectory() {
    return awsS3BucketRoot;
  }

  private String s3FileNameMetaTag() {
    return "original-file-name";
  }

  private Integer maxFileLimit() {
    return Integer.valueOf(awsMaxFileLimit);
  }


  @Bean
  public AWSInstance awsInstance() {
    log.debug("Creating AWSInstance.");
    var awsInstance = new AWSInstance(
        S3Client(),
        getS3ObjectRequestBuilder(),
        deleteS3ObjectRequestBuilder(),
        putS3ObjectRequestBuilder(),
        s3RootDirectory(),
        s3FileNameMetaTag(),
        maxFileLimit()
    );
    log.debug("Created AWSInstance '{}'.", awsInstance);
    return awsInstance;
  }
}
