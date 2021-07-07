package dev.cheem.projects.invoicestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import dev.cheem.projects.invoicestore.model.AWSInstance;
import dev.cheem.projects.invoicestore.model.DatabaseInstance;
import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import dev.cheem.projects.invoicestore.model.User;
import dev.cheem.projects.invoicestore.repository.UserRepository;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private UserService userService;

  @Mock
  private DatabaseInstance databaseInstance;

  @Mock
  private AWSInstance awsInstance;

  @Mock
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<Example<User>> findOneCaptor;

  @Captor
  private ArgumentCaptor<Long> findByIdCaptor;

  @Captor
  private ArgumentCaptor<User> saveCaptor;

  @BeforeEach
  public void setUp() {
    initMocks(databaseInstance);
    initMocks(awsInstance);
    initMocks(userRepository);
    userService = new UserService(databaseInstance, awsInstance, userRepository);
  }

  @Nested
  class LoginTests {


    @Test
    public void givenNoUsersExist_whenLoginCalled_thenRepositoryCheckedForNewUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given fresh repository
      when(userRepository.findOne(any())).thenReturn(Optional.empty());
      when(userRepository.save(eq(exampleUser))).thenReturn(exampleUserWithId);

      // when login called with new user name
      userService.login(oAuth2Id);

      // then repository was checked for the existing user
      verify(userRepository, times(1)).findOne(findOneCaptor.capture());
      assertEquals(Example.of(exampleUser), findOneCaptor.getValue());
    }

    @Test
    public void givenNoUsersExist_whenLoginCalled_thenRepositorySavedWithNewUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given fresh repository
      when(userRepository.findOne(any())).thenReturn(Optional.empty());
      when(userRepository.save(eq(exampleUser))).thenReturn(exampleUserWithId);

      // when login called with new user name
      userService.login(oAuth2Id);

      // then repository save method was called with correct user information
      verify(userRepository, times(1)).save(saveCaptor.capture());
      assertEquals(exampleUser.getOAuth2Id(), saveCaptor.getValue().getOAuth2Id());
    }

    @Test
    public void givenNoUsersExist_whenLoginCalled_thenLoginReturnsNewUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given fresh repository
      when(userRepository.findOne(any())).thenReturn(Optional.empty());
      when(userRepository.save(exampleUser)).thenReturn(exampleUserWithId);

      // when login called with new user name
      var loggedInUser = userService.login(oAuth2Id);

      // then login result has correct values set
      assertEquals(loggedInUser, exampleUserWithId);
    }

    @Test
    public void givenUserAlreadyExists_whenLoginCalled_thenRepositoryCheckedForNewUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given repository which already has the user
      when(userRepository.findOne(eq(Example.of(exampleUser))))
          .thenReturn(Optional.of(exampleUserWithId));

      // when login called with new user name
      userService.login(oAuth2Id);

      // then repository was checked for the existing user
      verify(userRepository, times(1)).findOne(findOneCaptor.capture());
      assertEquals(Example.of(exampleUser), findOneCaptor.getValue());
    }

    @Test
    public void givenUserAlreadyExists_whenLoginCalled_thenRepositoryNotSavedWithNewUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given repository which already has the user
      when(userRepository.findOne(eq(Example.of(exampleUser))))
          .thenReturn(Optional.of(exampleUserWithId));

      // when login called with same user name
      userService.login(oAuth2Id);

      // then the save method is not called
      verify(userRepository, never()).save(saveCaptor.capture());

    }

    @Test
    public void givenUserAlreadyExists_whenLoginCalled_thenLoginReturnsExistingUser() {
      var oAuth2Id = "oAuth2Id";
      var userId = 1L;

      var exampleUser = new User();
      exampleUser.setOAuth2Id(oAuth2Id);

      var exampleUserWithId = new User();
      exampleUserWithId.setUserId(userId);
      exampleUserWithId.setOAuth2Id(oAuth2Id);

      // given repository which already has the user
      when(
          userRepository.findOne(eq(Example.of(exampleUser)))
      ).thenReturn(
          Optional.of(exampleUserWithId)
      );

      // when login called with same user name
      var loggedInUser = userService.login(oAuth2Id);

      // then the login result matches the existing user
      assertEquals(loggedInUser, exampleUserWithId);
    }

    @Test
    public void givenNullOAuth2Id_whenLoginCalled_thenThrowsNullPointerException() {
      // given null OAuth2 id
      var oAuth2Id = (String) null;

      // when login called
      var login = (Executable) () -> userService.login(oAuth2Id);

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, login);
    }

    @Test
    public void givenNullOAuth2Id_whenLoginCalled_thenDoesNotCallRepositoryWithNull() {
      // given null OAuth2 id
      var oAuth2Id = (String) null;

      // when login called
      try {
        userService.login(oAuth2Id);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).findOne(findOneCaptor.capture());
      verify(userRepository, never()).save(saveCaptor.capture());
    }

  }

  @Nested
  class CheckUserTests {

    @Test
    public void givenNoUsersExist_whenCheckUserCalled_thenCheckUserReturnsEmpty() {
      // given no users exist
      var userId = 1L;
      when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

      // when check user called
      var result = userService.checkUser(userId);

      // then returns empty
      assertTrue(result.isEmpty());
    }

    @Test
    public void givenUserExists_whenCheckUserCalled_thenCheckUserReturnsUserId() {
      // given no users exist
      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      when(userRepository.findById(any())).thenReturn(Optional.of(user));

      // when check user called
      var result = userService.checkUser(userId);

      // then returns user id
      assertFalse(result.isEmpty());
      assertEquals(userId, result.get());
    }

    @Test
    public void givenNullUserId_whenCheckUserCalled_thenThrowsNullPointerException() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when check user called
      var checkUser = (Executable) () -> userService.checkUser(userId);

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, checkUser);

    }

    @Test
    public void givenNullUserId_whenCheckUserCalled_thenDoesNotCallRepositoryWithNull() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when check user called
      try {
        userService.checkUser(userId);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).findById(findByIdCaptor.capture());

    }

  }

  @Nested
  class GetUserTests {

    @Test
    public void givenNoUsersExist_whenGetUserCalled_thenCheckUserThrows() {
      // given no users exist
      var userId = 1L;
      when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

      // when get user called
      var getUser = (Executable) () -> userService.getUser(userId);

      // then throws NoSuchElementException
      assertThrows(NoSuchElementException.class, getUser);
    }

    @Test
    public void givenUserExists_whenGetUserCalled_thenCheckUserReturnsUser() {
      // given no users exist
      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      when(userRepository.findById(any())).thenReturn(Optional.of(user));

      // when check user called
      var result = userService.getUser(userId);

      // then returns user
      assertEquals(user, result);
    }

    @Test
    public void givenNullUserId_whenGetUserCalled_thenThrowsNullPointerException() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when get user called
      var checkUser = (Executable) () -> userService.getUser(userId);

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, checkUser);

    }

    @Test
    public void givenNullUserId_whenGetUserCalled_thenDoesNotCallRepositoryWithNull() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when get user called
      try {
        userService.getUser(userId);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).findById(findByIdCaptor.capture());

    }

  }

  @Nested
  class DetailsMatchTests {

    @Test
    public void givenUserWithMatchingInvoice_whenDetailsMatchCalled_thenReturnsTrue() {
      // given user with matching invoice
      var userId = 1L;
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));
      when(userRepository.getOne(eq(userId))).thenReturn(user);

      // when details match called
      var detailsMatch = userService.detailsMatch(userId, invoiceDetailsId);

      // then returns true
      assertTrue(detailsMatch);
    }

    @Test
    public void givenUserWithMatchingInvoice_whenDetailsMatchCalled_thenRepositoryCheckedForUser() {
      // given user with matching invoice
      var userId = 1L;
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));
      when(userRepository.getOne(eq(userId))).thenReturn(user);

      // when details match called
      userService.detailsMatch(userId, invoiceDetailsId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenUserWithMismatchingInvoice_whenDetailsMatchCalled_thenReturnsFalse() {
      // given user with matching invoice
      var userId = 1L;
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));
      when(userRepository.getOne(eq(userId))).thenReturn(user);

      // when details match called
      var detailsMatch = userService.detailsMatch(userId, "fake id");

      // then returns true
      assertFalse(detailsMatch);
    }

    @Test
    public void givenUserWithMismatchingInvoice_whenDetailsMatchCalled_thenRepositoryCheckedForUser() {
      // given user with matching invoice
      var userId = 1L;
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));
      when(userRepository.getOne(eq(userId))).thenReturn(user);

      // when details match called
      userService.detailsMatch(userId, "fake id");

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenNullUserId_whenDetailsMatchCalled_thenThrowsNullPointerException() {
      // given null OAuth2 id
      var userId = (Long) null;
      var invoiceDetailsId = "id";

      // when details match called
      var detailsMatch = (Executable) () -> userService.detailsMatch(userId, invoiceDetailsId);

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, detailsMatch);

    }

    @Test
    public void givenNullUserId_whenDetailsMatchCalled_thenDoesNotCallRepositoryWithNull() {
      // given null OAuth2 id
      var userId = (Long) null;
      var invoiceDetailsId = "id";

      // when details match called
      try {
        userService.detailsMatch(userId, invoiceDetailsId);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).getOne(findByIdCaptor.capture());

    }

    @Test
    public void givenNullInvoiceDetailsId_whenDetailsMatchCalled_thenThrowsNullPointerException() {
      // given null invoice details id
      var userId = 1L;
      var invoiceDetailsId = (String) null;

      // when details match called
      var detailsMatch = (Executable) () -> userService.detailsMatch(userId, invoiceDetailsId);

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, detailsMatch);

    }

    @Test
    public void givenNullInvoiceDetailsId_whenDetailsMatchCalled_thenDoesNotCallRepository() {
      // given null invoice details id
      var userId = 1L;
      var invoiceDetailsId = (String) null;

      // when details match called
      try {
        userService.detailsMatch(userId, invoiceDetailsId);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).getOne(findByIdCaptor.capture());

    }
  }

  @Nested
  class AllowedMoreFilesTests {

    @Test
    public void givenUserWithNoInvoicesAndLimitOfZero_whenAllowedMoreFilesCalled_thenReturnsFalse() {
      // given user with no invoices and limit of zero
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Collections.emptySet());

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(0);

      // when allowed more files called
      var detailsMatch = userService.allowedMoreFiles(userId);

      // then returns false
      assertFalse(detailsMatch);
    }

    @Test
    public void givenUserWithNoInvoicesAndLimitOfZero_whenAllowedMoreFilesCalled_thenRepositoryCheckedForUser() {
      // given user with no invoices and limit of zero
      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Collections.emptySet());

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(0);

      // when allowed more files called
      userService.allowedMoreFiles(userId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenUserWithNoInvoicesAndLimitOfOne_whenAllowedMoreFilesCalled_thenReturnsTrue() {
      // given user with no invoices and limit of one
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Collections.emptySet());

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      var detailsMatch = userService.allowedMoreFiles(userId);

      // then returns true
      assertTrue(detailsMatch);
    }

    @Test
    public void givenUserWithNoInvoicesAndLimitOfOne_whenAllowedMoreFilesCalled_thenRepositoryCheckedForUser() {
      // given user with no invoices and limit of one
      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Collections.emptySet());

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      userService.allowedMoreFiles(userId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenUserWithOneInvoiceAndLimitOfOne_whenAllowedMoreFilesCalled_thenReturnsFalse() {
      // given user with one invoice and limit of one
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      var detailsMatch = userService.allowedMoreFiles(userId);

      // then returns false
      assertFalse(detailsMatch);
    }

    @Test
    public void givenUserWithOneInvoiceAndLimitOfOne_whenAllowedMoreFilesCalled_thenRepositoryCheckedForUser() {
      // given user with one invoices and limit of one
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      userService.allowedMoreFiles(userId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenUserWithOneInvoiceAndLimitOfTen_whenAllowedMoreFilesCalled_thenReturnsTrue() {
      // given user with one invoice and limit of ten
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(10);

      // when allowed more files called
      var detailsMatch = userService.allowedMoreFiles(userId);

      // then returns true
      assertTrue(detailsMatch);
    }

    @Test
    public void givenUserWithOneInvoiceAndLimitOfTen_whenAllowedMoreFilesCalled_thenRepositoryCheckedForUser() {
      // given user with one invoices and limit of ten
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(10);

      // when allowed more files called
      userService.allowedMoreFiles(userId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenUserWithTwoInvoicesAndLimitOfOne_whenAllowedMoreFilesCalled_thenReturnsFalse() {
      // given user with two invoices and limit of one
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var invoiceDetailsId2 = "id2";
      var invoice2 = new InvoiceDetails();
      invoice2.setInvoiceDetailsId(invoiceDetailsId2);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice, invoice2));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      var detailsMatch = userService.allowedMoreFiles(userId);

      // then returns false
      assertFalse(detailsMatch);
    }

    @Test
    public void givenUserWithTwoInvoicesAndLimitOfOne_whenAllowedMoreFilesCalled_thenRepositoryCheckedForUser() {
      // given user with two invoices and limit of one
      var invoiceDetailsId = "id";
      var invoice = new InvoiceDetails();
      invoice.setInvoiceDetailsId(invoiceDetailsId);
      var invoiceDetailsId2 = "id2";
      var invoice2 = new InvoiceDetails();
      invoice2.setInvoiceDetailsId(invoiceDetailsId2);

      var userId = 1L;
      var user = new User();
      user.setUserId(userId);
      user.setInvoiceDetailsSet(Set.of(invoice, invoice2));

      when(userRepository.getOne(eq(userId))).thenReturn(user);
      when(awsInstance.getMaxFileLimit()).thenReturn(1);

      // when allowed more files called
      userService.allowedMoreFiles(userId);

      // then repository checked for user
      verify(userRepository, times(1)).getOne(eq(userId));
    }

    @Test
    public void givenNullUserId_whenAllowedMoreFilesCalled_thenThrowsNullPointerException() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when allowed more files called
      var detailsMatch = (Executable) () -> {
        userService.allowedMoreFiles(userId);
      };

      // then throws NullPointerException, not HibernateException
      assertThrows(NullPointerException.class, detailsMatch);

    }

    @Test
    public void givenNullUserId_whenAllowedMoreFilesCalled_thenDoesNotCallRepositoryWithNull() {
      // given null OAuth2 id
      var userId = (Long) null;

      // when allowed more files called
      try {
        userService.allowedMoreFiles(userId);
      } catch (Exception ignored) {

      }

      // then does not call repository
      verify(userRepository, never()).getOne(findByIdCaptor.capture());

    }
  }

  @Nested
  class AllowedMoreUsersTests {

    @Test
    public void givenNoUsersExistAndLimitOfZero_whenAllowedMoreUsersCalled_thenReturnsFalse() {
      // given no users exist and limit of 0
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(0);

      // when allowed more users called
      var allowedMoreUsers = userService.allowedMoreUsers();

      // then returns false
      assertFalse(allowedMoreUsers);
    }

    @Test
    public void givenNoUsersExistAndLimitOfZero_whenAllowedMoreUsersCalled_thenRepositoryCountChecked() {
      // given no users exist and limit of 0
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(0);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(userRepository, times(1)).count();
    }

    @Test
    public void givenNoUsersExistAndLimitOfZero_whenAllowedMoreUsersCalled_thenDatabaseInstanceLimitChecked() {
      // given no users exist and limit of 0
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(0);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(databaseInstance, times(1)).getMaxUserLimit();
    }

    @Test
    public void givenNoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenReturnsTrue() {
      // given no users exist and limit of 1
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      var allowedMoreUsers = userService.allowedMoreUsers();

      // then returns true
      assertTrue(allowedMoreUsers);
    }

    @Test
    public void givenNoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenRepositoryCountChecked() {
      // given no users exist and limit of 1
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(userRepository, times(1)).count();
    }

    @Test
    public void givenNoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenDatabaseInstanceLimitChecked() {
      // given no users exist and limit of 1
      when(userRepository.count()).thenReturn(0L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(databaseInstance, times(1)).getMaxUserLimit();
    }

    @Test
    public void givenOneUserExistsAndLimitOfOne_whenAllowedMoreUsersCalled_thenReturnsFalse() {
      // given one user exists and limit of 1
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      var allowedMoreUsers = userService.allowedMoreUsers();

      // then returns false
      assertFalse(allowedMoreUsers);
    }

    @Test
    public void givenOneUserExistsAndLimitOfOne_whenAllowedMoreUsersCalled_thenRepositoryCountChecked() {
      // given one user exists and limit of 1
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(userRepository, times(1)).count();
    }

    @Test
    public void givenOneUserExistsAndLimitOfOne_whenAllowedMoreUsersCalled_thenDatabaseInstanceLimitChecked() {
      // given one user exists and limit of 1
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(databaseInstance, times(1)).getMaxUserLimit();
    }

    @Test
    public void givenOneUserExistsAndLimitOfTen_whenAllowedMoreUsersCalled_thenReturnsTrue() {
      // given one user exists and limit of 10
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(10);

      // when allowed more users called
      var allowedMoreUsers = userService.allowedMoreUsers();

      // then returns true
      assertTrue(allowedMoreUsers);
    }

    @Test
    public void givenOneUserExistsAndLimitOfTen_whenAllowedMoreUsersCalled_thenRepositoryCountChecked() {
      // given one user exists and limit of 10
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(10);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(userRepository, times(1)).count();
    }

    @Test
    public void givenOneUseExistsAndLimitOfTen_whenAllowedMoreUsersCalled_thenDatabaseInstanceLimitChecked() {
      // given one user exists and limit of 10
      when(userRepository.count()).thenReturn(1L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(10);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(databaseInstance, times(1)).getMaxUserLimit();
    }

    @Test
    public void givenTwoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenReturnsFalse() {
      // given two user exists and limit of 1
      when(userRepository.count()).thenReturn(2L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      var allowedMoreUsers = userService.allowedMoreUsers();

      // then returns false
      assertFalse(allowedMoreUsers);
    }

    @Test
    public void givenTwoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenRepositoryCountChecked() {
      // given two user exists and limit of 1
      when(userRepository.count()).thenReturn(2L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(userRepository, times(1)).count();
    }

    @Test
    public void givenTwoUsersExistAndLimitOfOne_whenAllowedMoreUsersCalled_thenDatabaseInstanceLimitChecked() {
      // given two user exists and limit of 1
      when(userRepository.count()).thenReturn(2L);
      when(databaseInstance.getMaxUserLimit()).thenReturn(1);

      // when allowed more users called
      userService.allowedMoreUsers();

      // then repository checked for count
      verify(databaseInstance, times(1)).getMaxUserLimit();
    }
  }
}