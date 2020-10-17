package dev.cheem.projects.invoicestore.exception;

public class GraphQLMappingException extends InvoiceStoreException {

  public GraphQLMappingException() {
    super();
  }

  public GraphQLMappingException(String message) {
    super(message);
  }

  public GraphQLMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public GraphQLMappingException(Throwable cause) {
    super(cause);
  }

  protected GraphQLMappingException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
