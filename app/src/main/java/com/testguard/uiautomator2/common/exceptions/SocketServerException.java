package com.testguard.uiautomator2.common.exceptions;

@SuppressWarnings("serial")
public class SocketServerException extends Exception {

  String reason;

  /**
   * Exception for socket errors.
   *
   * @param msg
   *          A descriptive message describing the error.
   */
  public SocketServerException(final String msg) {
    super(msg);
    reason = msg;
  }

  public String getError() {
    return reason;
  }
}
