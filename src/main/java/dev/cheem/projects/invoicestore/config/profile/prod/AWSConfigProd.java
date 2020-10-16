package dev.cheem.projects.invoicestore.config.profile.prod;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Setter
@Configuration("awsConfig")
@Slf4j
public class AWSConfigProd {

  @Value("${AWS_S3_BUCKET}")
  private String awsS3BucketProperty;

  @Value("${AWS_REGION}")
  private String awsRegionProperty;

  @Value("${AWS_ENDPOINT}")
  private String awsEndpointProperty;

  @Value("${AWS_S3_BUCKET_ROOT}")
  private String awsS3BucketRootProperty;

  @Value("${AWS_MAX_FILE_LIMIT}")
  private String awsMaxFileLimitProperty;

  @Value("${AWS_ACCESS_KEY_ID}")
  private String awsAccessKeyIdProperty;

  @Value("${AWS_SECRET_ACCESS_KEY}")
  private String awsSecretAccessKeyProperty;

  @Bean
  public String awsS3Bucket() {
    return this.awsS3BucketProperty;
  }

  @Bean
  public String awsRegion() {
    return this.awsRegionProperty;
  }

  @Bean
  public String awsEndpoint() {
    return this.awsEndpointProperty;
  }

  @Bean
  public String awsS3BucketRoot() {
    return this.awsS3BucketRootProperty;
  }

  @Bean
  public String awsMaxFileLimit() {
    return this.awsMaxFileLimitProperty;
  }

  @Bean
  public String awsAccessKeyId() {
    return this.awsAccessKeyIdProperty;
  }

  @Bean
  public String awsSecretAccessKey() {
    return this.awsSecretAccessKeyProperty;
  }

}
