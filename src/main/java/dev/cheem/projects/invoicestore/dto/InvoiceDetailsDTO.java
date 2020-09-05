package dev.cheem.projects.invoicestore.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class InvoiceDetailsDTO {

  private Long invoiceDetailsId;

  private Date invoiceDate;

  private String invoiceName;

  private BigDecimal invoiceTotalVAT;

  private BigDecimal invoiceTotal;

  private InvoiceFileDTO invoiceFile;

  private Boolean invoiceArchived;

}
