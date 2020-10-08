package dev.cheem.projects.invoicestore.dto.graphql;

import lombok.Data;

@Data
public class GraphQLInvoiceDTO {

  private String id;
  private String date;
  private String name;
  private Double total;
  private Double vatTotal;
  private Boolean archived;
  private GraphQLInvoiceFileDTO invoiceFile;

}
