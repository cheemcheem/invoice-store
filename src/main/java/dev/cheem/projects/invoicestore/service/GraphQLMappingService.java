package dev.cheem.projects.invoicestore.service;

import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceDTO;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceFileDTO;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLUserDTO;
import dev.cheem.projects.invoicestore.exception.GraphQLMappingException;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.util.LocalDateTimeConverter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Creates mapping methods to be used by other GraphQL services to convert between GraphQL DTOs and
 * in-app classes. These methods often take Supplier instances rather than the object themselves so
 * that data can be requested as needed.
 */
@Slf4j
@Service
public class GraphQLMappingService {

  public Function<InvoiceDetails, GraphQLInvoiceDTO> mapInvoiceFields(@NonNull Set<String> fields) {
    return invoiceDetails -> {
      log.debug("GraphQLMappingService.mapInvoiceFields");
      var graphQLInvoiceDTO = new GraphQLInvoiceDTO();
      graphQLInvoiceDTO.setId(invoiceDetails.getInvoiceDetailsId());

      fields.forEach(field -> {
        switch (field) {
          case "id":
            break;
          case "date":
            graphQLInvoiceDTO.setDate(
                LocalDateTimeConverter.formatISO(invoiceDetails.getInvoiceDate())
            );
            break;
          case "name":
            graphQLInvoiceDTO.setName(invoiceDetails.getInvoiceName());
            break;
          case "total":
            graphQLInvoiceDTO.setTotal(invoiceDetails.getInvoiceTotal().doubleValue());
            break;
          case "vatTotal":
            graphQLInvoiceDTO.setVatTotal(invoiceDetails.getInvoiceTotalVAT().doubleValue());
            break;
          case "archived":
            graphQLInvoiceDTO.setArchived(invoiceDetails.getInvoiceArchived());
            break;
          case "invoiceFile":
            break;
          default:
            throw new GraphQLMappingException(
                "Invalid field '" + field + "' when mapping invoice fields."
            );
        }
      });
      return graphQLInvoiceDTO;
    };
  }

  public Function<Map<String, Object>, InvoiceDetails> mapInvoiceInputFields(
      @NonNull Supplier<User> user
  ) {
    return invoiceInput -> {
      log.debug("GraphQLMappingService.mapInvoiceInputFields");
      var invoice = new InvoiceDetails();
      invoice.setInvoiceUser(user.get());
      invoice.setInvoiceArchived(false);

      log.debug("invoiceInput '{}' ", invoiceInput);
      invoice.setInvoiceDate(LocalDateTimeConverter.parseISO((String) invoiceInput.get("date")));
      invoice.setInvoiceName((String) invoiceInput.get("name"));
      invoice.setInvoiceTotal(BigDecimal.valueOf((Double) invoiceInput.get("total")));
      invoice.setInvoiceTotalVAT(BigDecimal.valueOf((Double) invoiceInput.get("vatTotal")));

      return invoice;
    };
  }


  public Supplier<GraphQLUserDTO> mapUserFields(
      @NonNull Supplier<Long> userId,
      @NonNull Supplier<String> name,
      @NonNull Supplier<String> picture,
      @NonNull Set<String> fields
  ) {
    return () -> {
      log.debug("GraphQLMappingService.mapUserFields");
      var graphQLUserDTO = new GraphQLUserDTO();
      graphQLUserDTO.setId(userId.get().toString());

      fields.forEach(field -> {
        switch (field) {
          case "id":
            break;
          case "name":
            graphQLUserDTO.setName(name.get());
            break;
          case "picture":
            graphQLUserDTO.setPicture(picture.get());
            break;
          case "invoices":
            break;
          default:
            throw new GraphQLMappingException(
                "Invalid field '" + field + "' when mapping user fields.");
        }
      });
      return graphQLUserDTO;
    };
  }

  public Supplier<GraphQLInvoiceFileDTO> mapFileFields(
      @NonNull Set<String> fields,
      @NonNull Supplier<String> name,
      @NonNull Supplier<String> type
  ) {
    return () -> {
      log.debug("GraphQLMappingService.mapFileFields");
      var graphQLInvoiceFileDTO = new GraphQLInvoiceFileDTO();
      fields.forEach(field -> {
        switch (field) {
          case "name":
            graphQLInvoiceFileDTO.setName(name.get());
            break;
          case "type":
            graphQLInvoiceFileDTO.setType(type.get());
            break;
          default:
            throw new GraphQLMappingException(
                "Invalid field '" + field + "' when mapping invoice file fields.");
        }
      });
      return graphQLInvoiceFileDTO;
    };
  }

  public Function<Map<String, Object>, InvoiceDetails> mapUpdateInputFields() {
    return invoiceInput -> {
      log.debug("GraphQLMappingService.mapUpdateInputFields");
      var invoice = new InvoiceDetails();
      log.debug("invoiceInput '{}' ", invoiceInput);
      invoice.setInvoiceDetailsId((String) invoiceInput.get("id"));
      invoice.setInvoiceDate(
          invoiceInput.get("date") != null
              ? LocalDateTimeConverter.parseISO((String) invoiceInput.get("date"))
              : null
      );
      invoice.setInvoiceName((String) invoiceInput.get("name"));
      invoice.setInvoiceTotal(
          invoiceInput.get("total") != null
              ? BigDecimal.valueOf((Double) invoiceInput.get("total"))
              : null
      );
      invoice.setInvoiceTotalVAT(
          invoiceInput.get("vatTotal") != null
              ? BigDecimal.valueOf((Double) invoiceInput.get("vatTotal"))
              : null
      );
      invoice.setInvoiceArchived((Boolean) invoiceInput.get("archived"));
      return invoice;
    };
  }

  public void updateInvoice(InvoiceDetails oldInvoice, InvoiceDetails updatedInvoice) {
    log.debug("GraphQLMappingService.updateInvoice");
    log.debug("oldInvoice '{}' ", oldInvoice);
    log.debug("updatedInvoice '{}' ", updatedInvoice);

    if (updatedInvoice.getInvoiceDate() != null) {
      oldInvoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
    }

    if (updatedInvoice.getInvoiceName() != null) {
      oldInvoice.setInvoiceName(updatedInvoice.getInvoiceName());
    }

    if (updatedInvoice.getInvoiceTotal() != null) {
      oldInvoice.setInvoiceTotal(updatedInvoice.getInvoiceTotal());
    }

    if (updatedInvoice.getInvoiceTotalVAT() != null) {
      oldInvoice.setInvoiceTotalVAT(updatedInvoice.getInvoiceTotalVAT());
    }

    if (updatedInvoice.getInvoiceArchived() != null) {
      oldInvoice.setInvoiceArchived(updatedInvoice.getInvoiceArchived());
    }
  }
}
