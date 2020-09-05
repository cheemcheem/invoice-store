package dev.cheem.projects.invoicestore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;

@ToString(exclude = {"data"})
@Data
@Entity
public class InvoiceFile {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long invoiceFileId;

  private String fileName;

  private String fileType;

  @Lob
  private byte[] data;

}
