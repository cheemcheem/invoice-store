package dev.cheem.projects.invoicestore.dto.graphql;

import java.util.List;
import lombok.Data;

@Data
public class GraphQLUserDTO {

  private String id;
  private String name;
  private String picture;
  private List<GraphQLInvoiceDTO> invoices;
}
