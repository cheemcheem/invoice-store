package dev.cheem.projects.invoicestore.dto.graphql;

import lombok.Data;

@Data
public class GraphQLInvoiceInputDTO {

  private String date;
  private String name;
  private Float total;
  private Float vatTotal;
}
