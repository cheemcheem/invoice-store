package dev.cheem.projects.invoicestore.dto.graphql;

import lombok.Data;

@Data
public class GraphQLInvoiceDTO {

  private String id;
  private String date;
  private String name;
  private Float total;
  private Float vatTotal;
  private Boolean archived;
  private GraphQLInvoiceFileDTO invoiceFile;

}
