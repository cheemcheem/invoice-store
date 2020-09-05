package dev.cheem.projects.invoicestore.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Entity
public class InvoiceDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long invoiceDetailsId;

  private Date invoiceDate;

  private String invoiceName;

  private BigDecimal invoiceTotal;

  private BigDecimal invoiceTotalVAT;

  @OneToOne
  @Nullable
  private InvoiceFile invoiceFile;

  private Boolean invoiceArchived;

}
