package dev.cheem.projects.invoicestore.util;

import dev.cheem.projects.invoicestore.exception.InvalidDateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class LocalDateTimeConverter {

  public static final String ISO_PATTERN = "yyyy-MM-dd";

  public synchronized static String formatISO(Date value) {
    return DateFormatUtils.format(value, ISO_PATTERN);
  }

  public synchronized static Date parseISO(String value) {
    try {
      return new SimpleDateFormat(ISO_PATTERN).parse(value);
    } catch (ParseException e) {
      throw new RuntimeException(InvalidDateException.wrongFormat(value, ISO_PATTERN));
    }
  }
}
