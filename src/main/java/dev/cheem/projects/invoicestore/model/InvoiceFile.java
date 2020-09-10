package dev.cheem.projects.invoicestore.model;

import static javax.persistence.FetchType.LAZY;

import java.sql.Blob;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import lombok.ToString;

@ToString(exclude = {"data"})
@Data
@Entity
public class InvoiceFile {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long invoiceFileId;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String fileType;

  @Lob
  @Basic(fetch = LAZY)
  private Blob data;

}
