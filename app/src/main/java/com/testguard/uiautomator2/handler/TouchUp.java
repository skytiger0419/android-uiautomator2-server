package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.core.UiAutomatorBridge;
import com.testguard.uiautomator2.utils.Logger;

/**
 * 松开按键
 * Created by Administrator on 2017/3/12.
 */

public class TouchUp extends TouchEvent {

    @Override
    protected boolean executeTouchEvent() throws UiObjectNotFoundException {
        printEventDebugLine("TouchUp");
        try {
            Logger.debug(clickX +":" + clickY);
            return UiAutomatorBridge.getInstance().getInteractionController().touchUp(clickX, clickY);
        } catch (final Exception e) {
            Logger.debug("Problem invoking touchUp: " + e);
            return false;
        }
    }
}
