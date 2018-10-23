package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.utils.Logger;
import com.testguard.uiautomator2.utils.Point;
import com.testguard.uiautomator2.utils.PositionHelper;
import org.json.JSONException;
import java.util.Hashtable;
import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 * 快速滑动
 * Created by Administrator on 2017/3/12.
 */

public class Flick extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        Point start = new Point(0.5, 0.5);
        Point end = new Point();
        Double steps;
        final Hashtable<String, Object> params = command.params();
        if (command.isElementCommand()) {
            AndroidElement el;
            try {
                el = command.getElement();
                start = el.getAbsolutePosition(start);
                final Integer xoffset = (Integer) params.get("xoffset");
                final Integer yoffset = (Integer) params.get("yoffset");
                final Integer speed = (Integer) params.get("speed");
                steps = 1250.0 / speed + 1;
                end.x = start.x + xoffset;
                end.y = start.y + yoffset;
            } catch (final Exception e) {
                return getErrorResult(e.getMessage());
            }
        } else {
            try {
                final Integer xSpeed = (Integer) params.get("xSpeed");
                final Integer ySpeed = (Integer) params.get("ySpeed");
                final Double speed = Math.min(1250.0,
                        Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
                steps = 1250.0 / speed + 1;
                start = PositionHelper.getDeviceAbsPos(start);
                end = calculateEndPoint(start, xSpeed, ySpeed);
            } catch (final InvalidCoordinatesException e) {
                return getErrorResult(e.getMessage());
            } catch (final UiObjectNotFoundException e) {
                return getErrorResult(e.getMessage());
            }
        }
        steps = Math.abs(steps);
        Logger.debug("Flicking from " + start.toString() + " to " + end.toString()
                + " with steps: " + steps.intValue());
        final boolean res = getInstance().getUiDevice().swipe(start.x.intValue(), start.y.intValue(),
                end.x.intValue(), end.y.intValue(), steps.intValue());
        if (res) {
            return getSuccessResult(res);
        } else {
            return getErrorResult("Flick did not complete successfully");
        }
    }


    private Point calculateEndPoint(final Point start, final Integer xSpeed,
                                    final Integer ySpeed) {
        final Point end = new Point();
        final double speedRatio = (double) xSpeed / ySpeed;
        double xOff;
        double yOff;

        final double value = Math.min(getInstance().getUiDevice().getDisplayHeight(), getInstance().getUiDevice().getDisplayWidth());

        if (speedRatio < 1) {
            yOff = value / 4;
            xOff = value / 4 * speedRatio;
        } else {
            xOff = value / 4;
            yOff = value / 4 / speedRatio;
        }

        xOff = Integer.signum(xSpeed) * xOff;
        yOff = Integer.signum(ySpeed) * yOff;

        end.x = start.x + xOff;
        end.y = start.y + yOff;
        return end;
    }
}
