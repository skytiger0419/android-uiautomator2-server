package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.core.UiAutomatorBridge;
import com.testguard.uiautomator2.utils.Logger;

/**
 * 按下按键
 * Created by Administrator on 2017/3/12.
 */

public class TouchDown extends TouchEvent {

    @Override
    protected boolean executeTouchEvent() throws UiObjectNotFoundException {
        printEventDebugLine("TouchDown");
        try {
            return UiAutomatorBridge.getInstance().getInteractionController().touchDown(clickX, clickY);
        } catch (final Exception e) {
            Logger.debug("Problem invoking touchDown: " + e);
            return false;
        }
    }
}
