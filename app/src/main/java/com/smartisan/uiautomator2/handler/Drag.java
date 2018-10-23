package com.smartisan.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.smartisan.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import com.smartisan.uiautomator2.utils.Point;
import com.smartisan.uiautomator2.utils.PositionHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

/**
 * This handler is used to drag in the Android UI.
 */
public class Drag extends BaseCommandHandler {


    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        // DragArguments is created on each execute which prevents leaking state
        // across executions.
        final DragArguments dragArgs = new DragArguments(command);
        if (command.isElementCommand()) {
            return dragElement(dragArgs);
        } else {
            return drag(dragArgs);
        }
    }

    /**
     * 拖动坐标
     *
     * @param dragArgs
     * @return
     */
    private AndroidCommandResult drag(final DragArguments dragArgs) {
        Point absStartPos = new Point();
        Point absEndPos = new Point();
        try {
            absStartPos = PositionHelper.getDeviceAbsPos(dragArgs.start);
            if (dragArgs.destEl != null) {
                absEndPos = dragArgs.destEl.getAbsolutePosition(absEndPos);
            } else {
                absEndPos = PositionHelper.getDeviceAbsPos(dragArgs.end);
            }
        } catch (final InvalidCoordinatesException e) {
            return getErrorResult(e.getMessage());
        } catch (final UiObjectNotFoundException e) {
            return getErrorResult(e.getMessage());
        }
        Logger.debug("Dragging from " + absStartPos.toString() + " to "
                + absEndPos.toString() + " with steps: " + dragArgs.steps.toString());
        final boolean res = Device.getInstance().getUiDevice().drag(absStartPos.x.intValue(),
                absStartPos.y.intValue(), absEndPos.x.intValue(),
                absEndPos.y.intValue(), dragArgs.steps);
        if (!res) {
            return getErrorResult("Drag did not complete successfully");
        }
        return getSuccessResult(res);
    }

    private AndroidCommandResult dragElement(final DragArguments dragArgs) {
        Point absEndPos = new Point();
        if (dragArgs.destEl == null) {
            try {
                absEndPos = PositionHelper.getDeviceAbsPos(dragArgs.end);
            } catch (final InvalidCoordinatesException e) {
                return getErrorResult(e.getMessage());
            } catch (final UiObjectNotFoundException e) {
                return getErrorResult(e.getMessage());
            }

            Logger.debug("Dragging the element with id " + dragArgs.el.getId()
                    + " to " + absEndPos.toString() + " with steps: "
                    + dragArgs.steps.toString());
            try {
                final boolean res = dragArgs.el.dragTo(absEndPos.x.intValue(),
                        absEndPos.y.intValue(), dragArgs.steps);
                if (!res) {
                    return getErrorResult("Drag did not complete successfully");
                } else {
                    return getSuccessResult(res);
                }
            } catch (final UiObjectNotFoundException e) {
                return getErrorResult("Drag did not complete successfully"
                        + e.getMessage());
            } catch (InvalidCoordinatesException e) {
                return new AndroidCommandResult(WDStatus.INVALID_ELEMENT_COORDINATES, e);
            }

        } else {
            Logger.debug("Dragging the element with id " + dragArgs.el.getId()
                    + " to destination element with id " + dragArgs.destEl.getId()
                    + " with steps: " + dragArgs.steps);
            try {
                final boolean res = dragArgs.el.dragTo(dragArgs.destEl.getUiObject(),
                        dragArgs.steps);
                if (!res) {
                    return getErrorResult("Drag did not complete successfully");
                } else {
                    return getSuccessResult(res);
                }
            } catch (final UiObjectNotFoundException e) {
                return getErrorResult("Drag did not complete successfully"
                        + e.getMessage());
            } catch (InvalidCoordinatesException e) {
                return new AndroidCommandResult(WDStatus.INVALID_ELEMENT_COORDINATES, e);
            }
        }

    }

    private static class DragArguments {

        public AndroidElement el;
        public AndroidElement destEl;
        public final Point start;
        public final Point end;
        public final Integer steps;

        public DragArguments(final AndroidCommand command) throws JSONException {

            final Hashtable<String, Object> params = command.params();
            try {
                if (params.get("elementId") != JSONObject.NULL) {
                    el = command.getElement();
                }
            } catch (final Exception e) {
                el = null;
            }
            try {
                if (params.get("destElId") != JSONObject.NULL) {
                    destEl = command.getDestElement();
                }
            } catch (final Exception e) {
                Logger.warning("destElid exception" + e.getMessage());
                destEl = null;
            }
            start = new Point(params.get("startX"), params.get("startY"));
            end = new Point(params.get("endX"), params.get("endY"));
            steps = (Integer) params.get("steps");
        }
    }
}
