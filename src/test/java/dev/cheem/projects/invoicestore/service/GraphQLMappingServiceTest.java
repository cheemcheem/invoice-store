package dev.cheem.projects.invoicestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import dev.cheem.projects.invoicestore.dto.graphql.GraphQLInvoiceDTO;
import dev.cheem.projects.invoicestore.dto.graphql.GraphQLUserDTO;
import dev.cheem.projects.invoicestore.exception.GraphQLMappingException;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.User;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
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

  @SuppressWarnings({"MagicConstant", "ResultOfMethodCallIgnored"})
  @Nested
  class MapInvoiceFields {

    @Mock
    private InvoiceDetails invoiceDetails;
    private String invoiceDetailsId;
    private Date invoiceDate;
    private String invoiceName;
    private BigDecimal invoiceTotal;
    private BigDecimal invoiceTotalVAT;
    private Boolean invoiceArchived;
    private int year;
    private int month;
    private int day;

    @BeforeEach
    public void setUp() {
      // set up values for mocks when needed
      invoiceDetailsId = "invoiceDetailsId";
      year = 2020;
      month = 12;
      day = 31;
      var calendar = Calendar.getInstance();
      calendar.set(year, month - 1, day, 0, 0, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      invoiceDate = calendar.getTime();
      invoiceName = "invoiceName";
      invoiceTotal = BigDecimal.valueOf(BigDecimal.TEN.doubleValue());
      invoiceTotalVAT = BigDecimal.valueOf(BigDecimal.ONE.doubleValue());
      invoiceArchived = true;

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
      var mapInvoiceFields = (Executable) () -> graphQLMappingService.mapInvoiceFields(fields)
          .apply(invoiceDetails);

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

  @SuppressWarnings("MagicConstant")
  @Nested
  class MapInvoiceInputFields {

    @Mock
    private InvoiceDetails invoiceDetails;

    @Mock
    public Map<String, Object> invoiceInputMap;

    @Mock
    private Supplier<User> getUser;

    private User user;
    private Date invoiceDate;
    private String invoiceName;
    private BigDecimal invoiceTotal;
    private BigDecimal invoiceTotalVAT;
    private int year;
    private int month;
    private int day;

    @BeforeEach
    public void setUp() {

      // set up mocks
      initMocks(invoiceDetails);
      initMocks(getUser);

      // set up values for mocks when needed
      user = new User();
      year = 2020;
      month = 12;
      day = 31;
      var calendar = Calendar.getInstance();
      calendar.set(year, month - 1, day, 0, 0, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      invoiceDate = calendar.getTime();
      invoiceName = "invoiceName";
      invoiceTotal = BigDecimal.valueOf(BigDecimal.TEN.doubleValue());
      invoiceTotalVAT = BigDecimal.valueOf(BigDecimal.ONE.doubleValue());
    }

    @Test
    public void givenValidInvoiceInputMap_whenMapInvoiceInputFieldsCalled_thenCreatesValidInvoice() {
      // given valid invoice input map
      when(invoiceInputMap.get(eq("date"))).thenReturn(year + "-" + month + "-" + day);
      when(invoiceInputMap.get(eq("name"))).thenReturn(invoiceName);
      when(invoiceInputMap.get(eq("total"))).thenReturn(invoiceTotal.doubleValue());
      when(invoiceInputMap.get(eq("vatTotal"))).thenReturn(invoiceTotalVAT.doubleValue());
      when(getUser.get()).thenReturn(user);

      // when map invoice input fields called
      var mapInvoiceDetails =
          graphQLMappingService.mapInvoiceInputFields(getUser).apply(invoiceInputMap);

      // the creates valid invoice
      var expected = new InvoiceDetails();
      expected.setInvoiceDate(invoiceDate);
      expected.setInvoiceName(invoiceName);
      expected.setInvoiceTotal(invoiceTotal);
      expected.setInvoiceTotalVAT(invoiceTotalVAT);
      expected.setInvoiceArchived(false);
      expected.setInvoiceUser(user);
      assertEquals(expected, mapInvoiceDetails);
    }

    @Test
    public void givenInvoiceInputMapWithMissingKeys_whenMapInvoiceInputFieldsCalled_thenThrowsNullPointerException() {
      // given invoice input map with with missing keys (value = null)
      when(invoiceInputMap.get(eq("date"))).thenReturn(null);
      when(getUser.get()).thenReturn(user);

      // when map invoice input fields called
      var mapInvoiceInputFields = (Executable) () -> graphQLMappingService
          .mapInvoiceInputFields(getUser).apply(invoiceInputMap);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceInputFields);
    }

    @Test
    public void givenNullInvoiceInputMap_whenMapInvoiceInputFieldsCalled_thenThrowsNullPointerException() {
      // given null invoice input map
      var invoiceInputMap = (Map<String, Object>) null;

      // when map invoice input fields called
      var mapInvoiceInputFields = (Executable) () -> graphQLMappingService
          .mapInvoiceInputFields(getUser).apply(invoiceInputMap);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceInputFields);
    }

    @Test
    public void givenValidInvoiceInputMapAndNullUserSupplier_whenMapInvoiceInputFieldsCalled_thenThrowsNullPointerException() {
      // given valid invoice input map and null user supplier
      var getUser = (Supplier<User>) null;

      // when map invoice input fields called
      var mapInvoiceInputFields = (Executable) () -> graphQLMappingService
          .mapInvoiceInputFields(getUser).apply(invoiceInputMap);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceInputFields);
    }

    @Test
    public void givenValidInvoiceInputMapAndNullUser_whenMapInvoiceInputFieldsCalled_thenThrowsNullPointerException() {
      // given valid invoice input map and null user
      when(getUser.get()).thenReturn(null);

      // when map invoice input fields called
      var mapInvoiceInputFields = (Executable) () -> graphQLMappingService
          .mapInvoiceInputFields(getUser).apply(invoiceInputMap);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapInvoiceInputFields);
    }

  }

  @Nested
  class MapUserFields {

    @Mock
    private Supplier<Long> userIdSupplier;

    @Mock
    private Supplier<String> nameSupplier;

    @Mock
    private Supplier<String> pictureSupplier;

    private Long userId;
    private String name;
    private String picture;

    @BeforeEach
    public void setUp() {
      // set up mocks
      initMocks(userIdSupplier);
      initMocks(nameSupplier);
      initMocks(pictureSupplier);

      // set up values for mocks when needed
      userId = 1L;
      name = "name";
      picture = "picture";
    }

    @Test
    public void givenValidSuppliersAndAllFields_whenMapUserFieldsCalled_thenCreatesValidDTO() {
      // given valid suppliers and fields
      var fields = Set.of("id", "name", "picture", "invoices");
      when(userIdSupplier.get()).thenReturn(userId);
      when(nameSupplier.get()).thenReturn(name);
      when(pictureSupplier.get()).thenReturn(picture);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      expected.setName(name);
      expected.setPicture(picture);
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenInvalidFields_whenMapUserFieldsCalled_thenThrowsGraphQLMappingException() {
      // given invalid fields
      var fields = Set.of("invalidField");
      when(userIdSupplier.get()).thenReturn(userId);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields);

      // then throws graph ql mapping exception
      assertThrows(GraphQLMappingException.class, mapUserFields::get);
    }

    @Test
    public void givenIDField_whenMapUserFieldsCalled_thenCreatesDTOWithID() {
      // given valid suppliers and id field
      var fields = Set.of("id", "name", "picture", "invoices");
      when(userIdSupplier.get()).thenReturn(userId);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO with id
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenNoFields_whenMapUserFieldsCalled_thenCreatesDTOWithID() {
      // given valid suppliers and no fields
      var fields = new HashSet<String>();
      when(userIdSupplier.get()).thenReturn(userId);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO with id
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenNameField_whenMapUserFieldsCalled_thenCreatesDTOWithNameAndID() {
      // given valid suppliers and name field
      var fields = Set.of("name");
      when(userIdSupplier.get()).thenReturn(userId);
      when(nameSupplier.get()).thenReturn(name);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO with name and id
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      expected.setName(name);
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenPictureField_whenMapUserFieldsCalled_thenCreatesDTOWithPictureAndID() {
      // given valid suppliers and picture field
      var fields = Set.of("picture");
      when(userIdSupplier.get()).thenReturn(userId);
      when(pictureSupplier.get()).thenReturn(picture);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO with picture and id
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      expected.setPicture(picture);
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenInvoiceField_whenMapUserFieldsCalled_thenCreatesDTOWithID() {
      // given valid suppliers and invoice field
      var fields = Set.of("invoices");
      when(userIdSupplier.get()).thenReturn(userId);

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields).get();

      // then creates valid DTO with id
      var expected = new GraphQLUserDTO();
      expected.setId(userId.toString());
      assertEquals(expected, mapUserFields);
    }

    @Test
    public void givenNullUserIdSupplier_whenMapUserFieldsCalled_thenThrowsNullPointerException() {
      // given null user id supplier
      var fields = new HashSet<String>();
      var userIdSupplier = (Supplier<Long>) null;

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapUserFields::get);

    }

    @Test
    public void givenNullNameSupplier_whenMapUserFieldsCalled_thenThrowsNullPointerException() {
      // given null name supplier
      var fields = new HashSet<String>();
      var nameSupplier = (Supplier<String>) null;

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapUserFields::get);
    }

    @Test
    public void givenNullPictureSupplier_whenMapUserFieldsCalled_thenThrowsNullPointerException() {
      // given null picture supplier
      var fields = new HashSet<String>();
      var pictureSupplier = (Supplier<String>) null;

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapUserFields::get);
    }

    @Test
    public void givenNullFields_whenMapUserFieldsCalled_thenThrowsNullPointerException() {
      // given null fields
      var fields = (Set<String>) null;

      // when map user fields called
      var mapUserFields = graphQLMappingService
          .mapUserFields(userIdSupplier, nameSupplier, pictureSupplier, fields);

      // then throws null pointer exception
      assertThrows(NullPointerException.class, mapUserFields::get);
    }

  }

}