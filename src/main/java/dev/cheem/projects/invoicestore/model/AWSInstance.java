package dev.cheem.projects.invoicestore.model;

import lombok.Data;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Data
public class AWSInstance {

  private final S3Client S3Client;
  private final GetObjectRequest.Builder getS3ObjectRequestBuilder;
  private final DeleteObjectRequest.Builder deleteS3ObjectRequestBuilder;
  private final PutObjectRequest.Builder putS3ObjectRequestBuilder;
  private final String s3RootDirectory;
  private final String s3FileNameMetaTag;
  private final Integer maxFileLimit;
}
