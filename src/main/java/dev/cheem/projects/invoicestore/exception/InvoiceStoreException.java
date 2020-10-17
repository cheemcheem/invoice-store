package dev.cheem.projects.invoicestore.exception;

public class InvoiceStoreException extends RuntimeException {

  public InvoiceStoreException() {
    super();
  }

  public InvoiceStoreException(String message) {
    super(message);
  }

  public InvoiceStoreException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvoiceStoreException(Throwable cause) {
    super(cause);
  }

  protected InvoiceStoreException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
