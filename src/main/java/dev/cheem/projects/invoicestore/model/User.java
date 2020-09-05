package dev.cheem.projects.invoicestore.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "user_table")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long userId;

  @Column(unique = true)
  private String oAuth2Id;

  @OneToMany(mappedBy = "invoiceUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<InvoiceDetails> invoiceDetailsSet;


}

