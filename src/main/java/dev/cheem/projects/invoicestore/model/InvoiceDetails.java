package dev.cheem.projects.invoicestore.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

@ToString(exclude = {"invoiceUser"})
@Getter
@Setter
@NoArgsConstructor
@Entity
public class InvoiceDetails {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String invoiceDetailsId;

  @Column(nullable = false)
  private Date invoiceDate;

  @Column(nullable = false)
  private String invoiceName;

  @Column(nullable = false)
  private BigDecimal invoiceTotal;

  @Column(nullable = false)
  private BigDecimal invoiceTotalVAT;

  @Nullable
  private String invoiceFile;

  @Column(nullable = false)
  private Boolean invoiceArchived;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User invoiceUser;

}
