package dev.cheem.projects.invoicestore.exception;

import java.time.LocalDateTime;

public class InvalidDateException extends InvoiceStoreException {

  private InvalidDateException(String message) {
    super(message);
  }

  public InvalidDateException(String invalidDate, Throwable cause) {
    super("Failed to parse '" + invalidDate + "' as Date. Reason: " + cause.getMessage() + ".");
  }

  public static InvalidDateException wrongWayAround(LocalDateTime invalidStartDate,
      LocalDateTime invalidEndDate) {
    return new InvalidDateException(
        "Start date '" + invalidStartDate + "' occurs after end date '" + invalidEndDate + "'."
    );
  }

  public static InvalidDateException wrongFormat(String invalidDate, String correctFormat) {
    return new InvalidDateException(
        "Date '" + invalidDate + "' is not in the required format '" + correctFormat + "'."
    );
  }
}
