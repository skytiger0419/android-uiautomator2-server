package com.smartisan.uiautomator2.utils;

/**
 * Simple static methods to be called at the start of your own methods to verify
 * correct arguments and state.
 */
public final class Preconditions {
  private Preconditions() {}

  /**
   * Ensures that an object reference passed as a parameter to the calling
   * method is not null.
   *
   * @param reference an object reference
   * @return the non-null reference that was validated
   * @throws NullPointerException if {@code reference} is null
   */
  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }
}
