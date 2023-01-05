package com.ccoins.bff.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;


public final class LoggerUtil {

  private static final ObjectWriter WRITTER =
          new ObjectMapper()
                  .registerModule(new JavaTimeModule())
                  .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                  .writer();

  private LoggerUtil() {
  }


  public static String getJson(final Object object) {
    String representation = Strings.EMPTY;
    try {
      representation = object != null? object.getClass().getSimpleName() : Strings.EMPTY;
      representation += WRITTER.writeValueAsString(object);
    } catch (Exception e) {
      representation = representation.concat("Error processing JSON");
    }
    return representation;
  }
}
