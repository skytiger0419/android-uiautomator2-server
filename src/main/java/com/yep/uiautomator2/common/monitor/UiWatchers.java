package com.yep.uiautomator2.common.monitor;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 监控集，用于界面监听
 */
public class UiWatchers {
  private static final String LOG_TAG = UiWatchers.class.getSimpleName();
  private final List<String>  mErrors = new ArrayList<String>();

  /**
   * We can use the UiDevice registerWatcher to register a small script to be
   * executed when the framework is waiting for a control to appear. Waiting may
   * be the cause of an unexpected dialog on the screen and it is the time when
   * the framework runs the registered watchers. This is a sample watcher
   * looking for ANR and crashes. it closes it and moves on. You should create
   * your own watchers and handle error logging properly for your type of tests.
   */
  public void registerAnrAndCrashWatchers() {

    UiDevice.getInstance().registerWatcher("ANR", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector()
            .className("com.android.server.am.AppNotRespondingDialog"));
        String errorText = null;
        if (window.exists()) {
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onAnrDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    // class names may have changed
    UiDevice.getInstance().registerWatcher("ANR2", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector().packageName("android")
            .textContains("isn't responding."));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onAnrDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    UiDevice.getInstance().registerWatcher("CRASH", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector()
            .className("com.android.server.am.AppErrorDialog"));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onCrashDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    UiDevice.getInstance().registerWatcher("CRASH2", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector().packageName("android")
            .textContains("has stopped"));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onCrashDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    Log.i(LOG_TAG, "Registed GUI Exception watchers");
  }

  public void onAnrDetected(String errorText) {
    mErrors.add(errorText);
  }

  public void onCrashDetected(String errorText) {
    mErrors.add(errorText);
  }

  public void reset() {
    mErrors.clear();
  }

  public List<String> getErrors() {
    return Collections.unmodifiableList(mErrors);
  }

  /**
   * Current implementation ignores the exception and continues.
   */
  public void postHandler() {
    // TODO: Add custom error logging here

    String formatedOutput = String.format("UI Exception Message: %-20s\n",
        UiDevice.getInstance().getCurrentPackageName());
    Log.e(LOG_TAG, formatedOutput);

    UiObject buttonOK = new UiObject(new UiSelector().text("OK").enabled(true));
    // sometimes it takes a while for the OK button to become enabled
    buttonOK.waitForExists(5000);
    try {
      buttonOK.click();
    } catch (UiObjectNotFoundException e) {
      Log.e(LOG_TAG, "Exception", e);
    }
  }
}