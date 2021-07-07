package dev.cheem.projects.invoicestore.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.cheem.projects.invoicestore.exception.InvoiceStoreException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalDateTimeConverterTest {

  @Nested
  class ParseISOTests {

    @Test
    public void givenValidDateString_whenValidDateStringParsed_thenReturnsDateEquivalent() {
      // given valid date string
      var expectedYear = 2020;
      var expectedMonth = 12;
      var expectedDay = 31;
      var dateString = expectedYear + "-" + expectedMonth + "-" + expectedDay;

      // when valid date string passed to converter
      var date = LocalDateTimeConverter.parseISO(dateString);

      // then returns date with correct values
      var instant = OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
      var actualYear = instant.getYear();
      var actualMonth = instant.getMonthValue();
      var actualDay = instant.getDayOfMonth();

      assertEquals(expectedYear, actualYear);
      assertEquals(expectedMonth, actualMonth);
      assertEquals(expectedDay, actualDay);
    }

    @Test
    public void givenInvalidDateString_whenInvalidDateStringParsed_thenThrows() {
      // given invalid date string
      var invalidDateString = "rubbish";

      // when invalid date string passed to converter
      var causeException = (Executable) () -> LocalDateTimeConverter.parseISO(invalidDateString);

      // then correct exception is thrown
      assertThrows(InvoiceStoreException.class, causeException);
    }
  }

  @Nested
  class FormatISOTests {

    @Test
    public void givenDateObject_whenDateFormatted_thenReturnsCorrectFormatOfString() {
      // given date object
      var expectedYear = 2020;
      var expectedMonth = 12;
      var expectedDay = 31;
      var calendar = Calendar.getInstance();
      calendar.set(expectedYear, expectedMonth - 1, expectedDay, 0, 0, 0);
      var date = calendar.getTime();

      // when date is formatted to a date string
      var actualDateString = LocalDateTimeConverter.formatISO(date);

      // then the returned date string matches the expected date string
      var expectedDateString = expectedYear + "-" + expectedMonth + "-" + expectedDay;
      assertEquals(expectedDateString, actualDateString);
    }

  }


}