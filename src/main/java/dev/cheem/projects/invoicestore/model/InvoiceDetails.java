package dev.cheem.projects.invoicestore.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@ToString(exclude = {"invoiceUser"})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class InvoiceDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long invoiceDetailsId;

  private Date invoiceDate;

  private String invoiceName;

  private BigDecimal invoiceTotal;

  private BigDecimal invoiceTotalVAT;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Nullable
  private InvoiceFile invoiceFile;

  private Boolean invoiceArchived;

  @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User invoiceUser;

}
