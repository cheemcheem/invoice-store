package dev.cheem.projects.invoicestore.util;

import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class LocalDateTimeConverter {

  public static final String ISO_PATTERN = "yyyy-MM-dd";

  public synchronized static String formatISO(Date value) {
    return DateFormatUtils.format(value, ISO_PATTERN);
  }
}
