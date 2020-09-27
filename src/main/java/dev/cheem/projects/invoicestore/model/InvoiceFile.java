package dev.cheem.projects.invoicestore.model;

import java.sql.Blob;
import lombok.Data;

@Data
public class InvoiceFile {

  private InvoiceFileDetails invoiceFileDetails;

  private Blob data;

}
