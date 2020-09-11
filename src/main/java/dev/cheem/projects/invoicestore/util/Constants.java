package dev.cheem.projects.invoicestore.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Constants {

  public static final String USER_ID_ATTRIBUTE_KEY = "USER_ID_ATTRIBUTE_KEY";
  public static final String USER_ID_SESSION_KEY = "USER_ID_SESSION_KEY";

  /**
   * A list of endpoints that are not required to have request attributes attached.
   */
  public static final Predicate<String> DO_NOT_INTERCEPT = Pattern.compile(
      "(/api/user)"
          + "|(/static/.*)"
          + "|(/[^/]*)"
  ).asMatchPredicate();
}
