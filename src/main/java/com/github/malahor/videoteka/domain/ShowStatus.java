package com.github.malahor.videoteka.domain;

import com.github.malahor.videoteka.util.DateHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum ShowStatus {
  UNLOCKED,
  LOCKED_CHANGED_CANCELED,
  LOCKED_NOT_IN_PRODUCTION,
  LOCKED_CHANGED_IN_PRODUCTION,
  LOCKED_IN_PRODUCTION,
  LOCKED_CHANGED_DATE,
  LOCKED_DATE_CONFIRMED,
  LOCKED_CHANGED_RELEASED;

  public static boolean isLocked(ShowEntity show) {
    return show.getShowStatus() != null && !show.getShowStatus().equals(UNLOCKED);
  }

  public static ShowStatus lockByDetails(ShowDetails details) {
    if (details.getContinuation() == null || !details.getContinuation().isInProduction())
      return LOCKED_NOT_IN_PRODUCTION;
    if (details.getContinuation().getReleaseDate() == null) return LOCKED_IN_PRODUCTION;
    if (LocalDate.now().isBefore(DateHandler.localDateOf(details.getContinuation().getReleaseDate())))
      return LOCKED_DATE_CONFIRMED;
    return UNLOCKED;
  }


  public ShowStatus changed() {
    return switch (this) {
      case LOCKED_CHANGED_CANCELED -> LOCKED_NOT_IN_PRODUCTION;
      case LOCKED_NOT_IN_PRODUCTION -> LOCKED_CHANGED_CANCELED;
      case LOCKED_CHANGED_IN_PRODUCTION -> LOCKED_IN_PRODUCTION;
      case LOCKED_IN_PRODUCTION -> LOCKED_CHANGED_IN_PRODUCTION;
      case LOCKED_CHANGED_DATE -> LOCKED_DATE_CONFIRMED;
      case LOCKED_DATE_CONFIRMED -> LOCKED_CHANGED_DATE;
      case LOCKED_CHANGED_RELEASED -> UNLOCKED;
      case UNLOCKED -> LOCKED_CHANGED_RELEASED;
    };
  }
}
