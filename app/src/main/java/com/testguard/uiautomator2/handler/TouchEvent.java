package com.testguard.uiautomator2.handler;

import android.graphics.Rect;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.testguard.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.utils.Logger;
import com.testguard.uiautomator2.utils.Point;
import com.testguard.uiautomator2.utils.PositionHelper;

import org.json.JSONException;
import java.util.Hashtable;

/**
 * 操作类基类
 * Created by Administrator on 2017/3/12.
 */

public abstract class TouchEvent extends BaseCommandHandler {
    protected AndroidElement                el;
    protected int                       clickX;
    protected int                       clickY;
    protected Hashtable<String, Object> params;
    protected boolean                   isElement;


    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        initalize();
        try {
            params = command.params();
            // isElementCommand doesn't check to see if we actually have an element
            // so getElement is used instead.
            try {
                if (command.getElement() != null) {
                    isElement = true;
                }
            } catch (final Exception e) {
                isElement = false;
            }
            if (isElement) {
                // extract x and y from the element.
                el = command.getElement();
                final Rect bounds = el.getBounds();
                clickX = bounds.centerX();
                clickY = bounds.centerY();
            } else { // no element so extract x and y from params
                final Object paramX = params.get("x");
                final Object paramY = params.get("y");
                // these will be defaulted to 0.5 when passed to getDeviceAbsPos
                double targetX = 0;
                double targetY = 0;
                if (paramX != null) {
                    targetX = Double.parseDouble(paramX.toString());
                }
                if (paramY != null) {
                    targetY = Double.parseDouble(paramY.toString());
                }
                Point coords = new Point(targetX, targetY);
                coords = PositionHelper.getDeviceAbsPos(coords);
                clickX = coords.x.intValue();
                clickY = coords.y.intValue();
            }
            if (executeTouchEvent()) {
                return getSuccessResult(true);
            }

        } catch (final UiObjectNotFoundException e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT, e.getMessage());
        } catch (final InvalidCoordinatesException e) {
            return new AndroidCommandResult(WDStatus.INVALID_ELEMENT_COORDINATES,
                    e.getMessage());
        } catch (final Exception e) {
            if(command.action().equalsIgnoreCase("touchup")){
                return getSuccessResult(true);
            }
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("Failed to execute touch event");
    }

    protected abstract boolean executeTouchEvent()
            throws UiObjectNotFoundException;

    private void initalize() {
        el = null;
        clickX = -1;
        clickY = -1;
        params = null;
        isElement = false;
    }

    protected void printEventDebugLine(final String methodName,
                                       final Integer... duration) {
        String extra = "";
        if (duration.length > 0) {
            extra = ", duration: " + duration[0];
        }
        Logger.debug("Performing " + methodName + " using element? " + isElement
                + " x: " + clickX + ", y: " + clickY + extra);
    }
}
