package com.testguard.uiautomator2.common.exceptions;


public class InvalidStrategyException extends Exception {
  /**
   * An exception that is thrown when an invalid strategy is used.
   *
   * @param msg
   *          A descriptive message describing the error.
   */
  public InvalidStrategyException(final String msg) {
    super(msg);
  }
}
