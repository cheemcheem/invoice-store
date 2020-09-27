package dev.cheem.projects.invoicestore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class InvoiceDetailsDTO {

  private Long invoiceDetailsId;

  private String invoiceDate;

  private String invoiceName;

  private BigDecimal invoiceTotalVAT;

  private BigDecimal invoiceTotal;

  private InvoiceFileDetailsDTO invoiceFile;

  private Boolean invoiceArchived;

}
