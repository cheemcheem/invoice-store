package dev.cheem.projects.invoicestore.config;

import dev.cheem.projects.invoicestore.service.GraphQLDataFetchers;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Setter
@Configuration
public class GraphQLConfig {

  private final GraphQLDataFetchers graphQLDataFetchers;

  @Value("classpath:graphql/invoiceStoreSchema.graphqls")
  private Resource invoiceStoreResource;

  @Bean
  public GraphQL graphQL() throws IOException {
    var sdl = getSDL();
    var schema = buildSchema(sdl);
    return GraphQL.newGraphQL(schema).build();
  }

  private String getSDL() throws IOException {
    var bytes = invoiceStoreResource.getInputStream().readAllBytes();
    return new String(bytes);
  }

  private GraphQLSchema buildSchema(String sdl) {
    var typeRegistry = new SchemaParser().parse(sdl);
    var runtimeWiring = buildWiring();
    var schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .type("Query", typeWiring -> typeWiring
            .dataFetcher("user", graphQLDataFetchers.getUserDataFetcher())
            .dataFetcher("invoiceById", graphQLDataFetchers.getInvoiceByIdDataFetcher())
        )
        .type("User", typeWiring -> typeWiring
            .dataFetcher("invoices", graphQLDataFetchers.getInvoicesDataFetcher())
        )
        .type("Invoice", typeWiring -> typeWiring
            .dataFetcher("invoiceFile", graphQLDataFetchers.getInvoiceFileDataFetcher())
        )
        .type("Mutation", typeWiring -> typeWiring
            .dataFetcher("createInvoice", graphQLDataFetchers.createInvoiceDataFetcher())
            .dataFetcher("updateInvoice", graphQLDataFetchers.updateInvoiceDataFetcher())
        )
        .build();
  }
}
