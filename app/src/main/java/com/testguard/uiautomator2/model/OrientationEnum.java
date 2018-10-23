package com.testguard.uiautomator2.model;

/**
 * An enumeration that mirrors {@link android.view.Surface}.
 *
 */
public enum OrientationEnum {
  ROTATION_0(0), ROTATION_90(1), ROTATION_180(2), ROTATION_270(3);

  public static OrientationEnum fromInteger(final int x) {
    switch (x) {
      case 0:
        return ROTATION_0;
      case 1:
        return ROTATION_90;
      case 2:
        return ROTATION_180;
      case 3:
        return ROTATION_270;
    }
    return null;
  }

  private final int value;

  private OrientationEnum(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public String getOrientation(){
    switch (this.getValue()) {
      case 0:
        return "PORTRAIT";
      case 1:
        return "LANDSCAPE RIGHT";
      case 2:
        return "PORTRAIT UPSIDE DOWN";
      case 3:
        return "LANDSCAPE LEFT";
    }
    return "UNKNOWN(" + this.getValue() + ")";
  }
}
