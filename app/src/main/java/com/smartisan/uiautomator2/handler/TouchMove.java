package com.smartisan.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.smartisan.uiautomator2.core.UiAutomatorBridge;
import com.smartisan.uiautomator2.utils.Logger;

/**
 * 称动到坐标
 * Created by Administrator on 2017/3/12.
 */

public class TouchMove extends TouchEvent {

    @Override
    protected boolean executeTouchEvent() throws UiObjectNotFoundException {
        printEventDebugLine("TouchMove");
        try {
            return UiAutomatorBridge.getInstance().getInteractionController().touchMove(clickX, clickY);
        } catch (final Exception e) {
            Logger.debug("Problem invoking touchMove: " + e);
            return false;
        }
    }
}
