package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceDTO;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceFileDTO;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLUserDTO;
import graphql.schema.DataFetcher;
import graphql.schema.SelectedField;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GraphQLDataFetchers {

  private final GraphQLInvoiceService graphQLInvoiceService;
  private final GraphQLInvoiceFileService graphQLInvoiceFileService;
  private final GraphQLUserService graphQLUserService;

  public DataFetcher<GraphQLUserDTO> getUserDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.getUserDataFetcher");
      var fields = getFields(dataFetchingEnvironment);
      return graphQLUserService.getUser(fields);
    };
  }

  public DataFetcher<List<GraphQLInvoiceDTO>> getInvoicesDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.getInvoicesDataFetcher");
      var userId = graphQLUserService.getUserId();
      var fields = getFields(dataFetchingEnvironment);
      return graphQLInvoiceService.getAllInvoiceDetailsGQL(userId, fields);
    };
  }

  private Set<String> getFields(graphql.schema.DataFetchingEnvironment dataFetchingEnvironment) {
    return dataFetchingEnvironment
        .getSelectionSet()
        .getFields()
        .stream()
        .filter(selectedField -> !selectedField.getQualifiedName()
            .contains("/")) // only fields for current level
        .map(SelectedField::getName)
        .collect(Collectors.toSet());
  }

  public DataFetcher<GraphQLInvoiceDTO> getInvoiceByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.getInvoiceByIdDataFetcher");
      var userId = graphQLUserService.getUserId();
      var invoiceId = dataFetchingEnvironment.getArgument("id").toString();
      var fields = getFields(dataFetchingEnvironment);
      return graphQLInvoiceService.getInvoiceById(
          userId,
          invoiceId,
          fields
      ).orElse(null);
    };
  }

  public DataFetcher<GraphQLInvoiceFileDTO> getInvoiceFileDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.getInvoiceFileDataFetcher");
      var userId = graphQLUserService.getUserId();
      var invoiceId = ((GraphQLInvoiceDTO) dataFetchingEnvironment.getSource()).getId();
      var fields = getFields(dataFetchingEnvironment);
      return graphQLInvoiceFileService.getInvoiceFileByInvoiceId(
          userId,
          invoiceId,
          fields
      ).orElse(null);
    };
  }

  public DataFetcher<GraphQLInvoiceDTO> createInvoiceDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.getCreateInvoiceDataFetcher");
      var userId = graphQLUserService.getUserId();
      Map<String, Object> input = dataFetchingEnvironment.getArgument("input");
      var fields = getFields(dataFetchingEnvironment);
      return graphQLInvoiceService.createInvoice(
          userId,
          input,
          fields
      ).orElse(null);
    };
  }

  public DataFetcher<GraphQLInvoiceDTO> updateInvoiceDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("GraphQLDataFetchers.updateInvoiceDataFetcher");
      var userId = graphQLUserService.getUserId();
      Map<String, Object> updated = dataFetchingEnvironment.getArgument("updated");
      var fields = getFields(dataFetchingEnvironment);
      return graphQLInvoiceService.updateInvoice(
          userId,
          updated,
          fields
      ).orElse(null);
    };
  }
}
