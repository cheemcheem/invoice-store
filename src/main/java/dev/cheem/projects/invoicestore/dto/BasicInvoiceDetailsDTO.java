package dev.cheem.projects.invoicestore.dto;

import lombok.Data;

@Data
public class BasicInvoiceDetailsDTO {

  private final String invoiceId;

  private final String invoiceDate;

  private final String invoiceName;

}
