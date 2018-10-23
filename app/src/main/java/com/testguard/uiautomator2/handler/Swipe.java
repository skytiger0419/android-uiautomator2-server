package com.testguard.uiautomator2.handler;

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

import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 * swipe滑动界面
 * Created by Administrator on 2017/3/12.
 */

public class Swipe extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        Point absStartPos, absEndPos;
        final boolean isSwipePerformed;
        try {
            final SwipeArguments swipeArgs;
            swipeArgs = new SwipeArguments(command);
            if (command.isElementCommand()) {
                absStartPos = swipeArgs.element.getAbsolutePosition(swipeArgs.start);
                absEndPos = swipeArgs.element.getAbsolutePosition(swipeArgs.end);
                Logger.debug("Swiping the element with ElementId " + swipeArgs.element.getId()
                        + " to " + absEndPos.toString() + " with steps: "
                        + swipeArgs.steps.toString());
            } else {
                absStartPos = PositionHelper.getDeviceAbsPos(swipeArgs.start);
                absEndPos = PositionHelper.getDeviceAbsPos(swipeArgs.end);
                Logger.debug("Swiping On Device from " + absStartPos.toString() + " to "
                        + absEndPos.toString() + " with steps: " + swipeArgs.steps.toString());
            }
            isSwipePerformed = getInstance().getUiDevice().swipe(absStartPos.x.intValue(),
                    absStartPos.y.intValue(), absEndPos.x.intValue(),
                    absEndPos.y.intValue(), swipeArgs.steps);
            if (!isSwipePerformed) {
                return getErrorResult("Swipe did not complete successfully");
            } else {
                return getSuccessResult(isSwipePerformed);
            }

        } catch (final UiObjectNotFoundException e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
        } catch (final InvalidCoordinatesException e) {
            return new AndroidCommandResult(WDStatus.INVALID_ELEMENT_COORDINATES, e);
        }
    }

    /**
     * 解析swipe参数
     */
    public class SwipeArguments {
        public final Point start;
        public final Point end;
        public final Integer steps;
        public AndroidElement element;

        public SwipeArguments(AndroidCommand command) throws JSONException {
            Hashtable<String, Object> params = command.params();
            if (command.isElementCommand()) {
                element = command.getElement();
            }
            start = new Point(params.get("startX"), params.get("startY"));
            end = new Point(params.get("endX"), params.get("endY"));
            steps = (Integer) params.get("steps");
        }
    }
}
