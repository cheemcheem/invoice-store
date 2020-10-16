package dev.cheem.projects.invoicestore.config.profile.none;

import io.findify.s3mock.S3Mock;
import java.net.URI;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.SocketUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Profile("!prod")
@Configuration("awsConfig")
@Slf4j
public class AWSConfig {

  private static final int PORT = SocketUtils.findAvailableTcpPort();
  private static final String TEST_ENDPOINT = "http://localhost:" + PORT;
  private static final String TEST_BUCKET = "bucket";
  private static final String TEST_REGION = "test_region";
  private static final String TEST_BUCKET_ROOT = "invoice_store";
  private static final String TEST_MAX_FILE_LIMIT = "100";
  private static final String TEST_ACCESS_KEY_ID = "test_access_key_id";
  private static final String TEST_AWS_SECRET_ACCESS_KEY = "test_aws_secret_access_key";

  static {
    log.debug("Setting up S3 Mock API.");
    S3Mock.create(PORT).start();
    // Handle this async to not block main thread that S3Mock runs on
    Executors.newSingleThreadExecutor().execute(() -> {
      var createBucketRequest = CreateBucketRequest.builder().bucket(TEST_BUCKET).build();
      var s3ClientBuilder = S3Client.builder()
          .region(Region.of(TEST_REGION))
          .endpointOverride(URI.create(TEST_ENDPOINT))
          .serviceConfiguration(builder -> builder.pathStyleAccessEnabled(true))
          .credentialsProvider(() -> AwsBasicCredentials.create(TEST_ACCESS_KEY_ID,TEST_AWS_SECRET_ACCESS_KEY));
      try (var s3Client = s3ClientBuilder.build()) {
        s3Client.createBucket(createBucketRequest);
      }
      log.debug("Finished setting up S3 Mock API.");
    });
  }

  @Bean
  public String awsS3Bucket() {
    return TEST_BUCKET;
  }

  @Bean
  public String awsRegion() {
    return TEST_REGION;
  }

  @Bean
  public String awsEndpoint() {
    return TEST_ENDPOINT;
  }

  @Bean
  public String awsS3BucketRoot() {
    return TEST_BUCKET_ROOT;
  }

  @Bean
  public String awsMaxFileLimit() {
    return TEST_MAX_FILE_LIMIT;
  }

  @Bean
  public String awsAccessKeyId() {
    return TEST_ACCESS_KEY_ID;
  }

  @Bean
  public String awsSecretAccessKey() {
    return TEST_AWS_SECRET_ACCESS_KEY;
  }
}
