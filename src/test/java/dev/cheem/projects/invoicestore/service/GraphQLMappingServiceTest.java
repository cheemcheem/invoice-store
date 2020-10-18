package dev.cheem.projects.invoicestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceDTO;
import dev.cheem.projects.invoicestore.exception.GraphQLMappingException;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GraphQLMappingServiceTest {

  private final GraphQLMappingService graphQLMappingService = new GraphQLMappingService();

  @Nested
  class MapInvoiceFields {

    @Mock
    public InvoiceDetails invoiceDetails;
    private String invoiceDetailsId;
    private Date invoiceDate;
    private String invoiceName;
    private BigDecimal invoiceTotal;
    private BigDecimal invoiceTotalVAT;
    private String invoiceFile;
    private Boolean invoiceArchived;
    private int year;
    private int month;
    private int day;
    private String invoiceFileName;
    private String invoiceFileType;


    @BeforeEach
    public void setUp() {
      // set up values for mocks when needed
      invoiceDetailsId = "invoiceDetailsId";
      year = 2020;
      month = 12;
      day = 31;
      var calendar = Calendar.getInstance();
      calendar.set(year, month - 1, day);
      invoiceDate = calendar.getTime();
      invoiceName = "invoiceName";
      invoiceTotal = BigDecimal.TEN;
      invoiceTotalVAT = BigDecimal.ONE;
      invoiceFile = UUID.randomUUID().toString();
      invoiceArchived = true;
      invoiceFileName = "invoiceFileName";
      invoiceFileType = "invoiceFileType";

      // set up mock
      initMocks(invoiceDetails);
    }


    @Test
    public void givenInvoiceAndNoFields_whenMapInvoiceFieldsCalled_thenOnlyIdFieldIsPopulated() {
      // given invoice and id field
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      var fields = new HashSet<String>();

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only id field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, only()).getInvoiceDetailsId();
    }

    @Test
    public void givenInvoiceAndIdField_whenMapInvoiceFieldsCalled_thenOnlyIdFieldIsPopulated() {
      // given invoice and id field
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      var fields = Set.of("id");

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only id field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, only()).getInvoiceDetailsId();

    }

    @Test
    public void givenInvoiceAndDateField_whenMapInvoiceFieldsCalled_thenOnlyDateAndIdFieldsArePopulated() {
      // given invoice and date field
      var fields = Set.of("date");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceDate()).thenReturn(invoiceDate);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only date field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setDate(year + "-" + month + "-" + day);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, times(1)).getInvoiceDate();
      verify(invoiceDetails, never()).getInvoiceName();
      verify(invoiceDetails, never()).getInvoiceTotal();
      verify(invoiceDetails, never()).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, never()).getInvoiceArchived();


    }

    @Test
    public void givenInvoiceAndNameField_whenMapInvoiceFieldsCalled_thenOnlyNameAndIdFieldsArePopulated() {
      // given invoice and name field
      var fields = Set.of("name");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceName()).thenReturn(invoiceName);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only name field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setName(invoiceName);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, never()).getInvoiceDate();
      verify(invoiceDetails, times(1)).getInvoiceName();
      verify(invoiceDetails, never()).getInvoiceTotal();
      verify(invoiceDetails, never()).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, never()).getInvoiceArchived();

    }

    @Test
    public void givenInvoiceAndTotalField_whenMapInvoiceFieldsCalled_thenOnlyTotalAndIdFieldsArePopulated() {
      // given invoice and total field
      var fields = Set.of("total");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceTotal()).thenReturn(invoiceTotal);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only total field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setTotal(invoiceTotal.doubleValue());
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, never()).getInvoiceDate();
      verify(invoiceDetails, never()).getInvoiceName();
      verify(invoiceDetails, times(1)).getInvoiceTotal();
      verify(invoiceDetails, never()).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, never()).getInvoiceArchived();
    }

    @Test
    public void givenInvoiceAndTotalVATField_whenMapInvoiceFieldsCalled_thenOnlyTotalVATAndIdFieldsArePopulated() {
      // given invoice and total vat field
      var fields = Set.of("vatTotal");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceTotalVAT()).thenReturn(invoiceTotalVAT);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only total vat field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setVatTotal(invoiceTotalVAT.doubleValue());
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, never()).getInvoiceDate();
      verify(invoiceDetails, never()).getInvoiceName();
      verify(invoiceDetails, never()).getInvoiceTotal();
      verify(invoiceDetails, times(1)).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, never()).getInvoiceArchived();
    }

    @Test
    public void givenInvoiceAndFileField_whenMapInvoiceFieldsCalled_thenOnlyIdFieldIsPopulated() {
      // given invoice and file field
      var fields = Set.of("invoiceFile");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only file field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, only()).getInvoiceDetailsId();
    }

    @Test
    public void givenInvoiceAndArchivedField_whenMapInvoiceFieldsCalled_thenOnlyArchivedAndIdFieldsArePopulated() {
      // given invoice and archived field
      var fields = Set.of("archived");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceArchived()).thenReturn(invoiceArchived);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then only archived field is populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setArchived(invoiceArchived);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, never()).getInvoiceDate();
      verify(invoiceDetails, never()).getInvoiceName();
      verify(invoiceDetails, never()).getInvoiceTotal();
      verify(invoiceDetails, never()).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, times(1)).getInvoiceArchived();
    }

    @Test
    public void givenInvoiceAndAllFields_whenMapInvoiceFieldsCalled_thenAllFieldsArePopulated() {
      // given invoice and all fields
      var fields = Set.of("id", "date", "name", "total", "vatTotal", "invoiceFile", "archived");
      when(invoiceDetails.getInvoiceDetailsId()).thenReturn(invoiceDetailsId);
      when(invoiceDetails.getInvoiceDate()).thenReturn(invoiceDate);
      when(invoiceDetails.getInvoiceName()).thenReturn(invoiceName);
      when(invoiceDetails.getInvoiceTotal()).thenReturn(invoiceTotal);
      when(invoiceDetails.getInvoiceTotalVAT()).thenReturn(invoiceTotalVAT);
      when(invoiceDetails.getInvoiceArchived()).thenReturn(invoiceArchived);

      // when map invoice fields called
      var mapInvoiceFields = graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then all expected fields are populated
      var expected = new GraphQLInvoiceDTO();
      expected.setId(invoiceDetailsId);
      expected.setDate(year + "-" + month + "-" + day);
      expected.setName(invoiceName);
      expected.setTotal(invoiceTotal.doubleValue());
      expected.setVatTotal(invoiceTotalVAT.doubleValue());
      expected.setArchived(invoiceArchived);
      assertEquals(expected, mapInvoiceFields);

      verify(invoiceDetails, times(1)).getInvoiceDetailsId();
      verify(invoiceDetails, times(1)).getInvoiceDate();
      verify(invoiceDetails, times(1)).getInvoiceName();
      verify(invoiceDetails, times(1)).getInvoiceTotal();
      verify(invoiceDetails, times(1)).getInvoiceTotalVAT();
      verify(invoiceDetails, never()).getInvoiceFile();
      verify(invoiceDetails, times(1)).getInvoiceArchived();
    }

    @Test
    public void givenInvoiceAndInvalidField_whenMapInvoiceFieldsCalled_thenThrowsGraphQLMappingException() {
      // given invoice and invalid field
      var fields = Set.of("invalidField");

      // when map invoice fields called
      var mapInvoiceFields = (Executable) () -> graphQLMappingService.mapInvoiceFields(fields).apply(invoiceDetails);

      // then graphql mapping exception thrown
      assertThrows(GraphQLMappingException.class, mapInvoiceFields);
    }

    @Test
    public void givenNullInvoiceAndNoFields_whenMapInvoiceFieldsCalled_thenThrowsNullPointerException() {
      // given null invoice and no fields
      var invoice = (InvoiceDetails) null;
      var fields = new HashSet<String>();

      // when map invoice fields called
      var mapInvoiceFields =
          (Executable) () -> graphQLMappingService.mapInvoiceFields(fields).apply(invoice);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceFields);

    }

    @Test
    public void givenInvoiceAndNullFields_whenMapInvoiceFieldsCalled_thenThrowsNullPointerException() {
      // given invoice and null fields
      var invoice = new InvoiceDetails();
      var fields = (Set<String>) null;

      // when map invoice fields called
      var mapInvoiceFields =
          (Executable) () -> graphQLMappingService.mapInvoiceFields(fields).apply(invoice);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceFields);

    }

  }
}