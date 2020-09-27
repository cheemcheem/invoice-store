package dev.cheem.projects.invoicestore.model;

import java.sql.Blob;
import java.time.Instant;
import lombok.Data;

@Data
public class InvoiceFile {

  private InvoiceFileDetails invoiceFileDetails;

  private Blob data;

  private String eTag;

  private Instant lastModified;
}
