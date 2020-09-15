package dev.cheem.projects.invoicestore.dto;

import lombok.Data;

@Data
public class InvoiceFileDTO {

  private String invoiceFileId;

  private String invoiceFileName;

  private String invoiceFileType;
}
