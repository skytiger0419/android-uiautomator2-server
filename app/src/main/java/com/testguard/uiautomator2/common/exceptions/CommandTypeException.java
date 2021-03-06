package com.testguard.uiautomator2.common.exceptions;


public class CommandTypeException extends Exception {
  /**
   * Exception for command type errors.
   *
   * @param msg
   *          A descriptive message describing the error.
   */
  public CommandTypeException(final String msg) {
    super(msg);
  }
}
