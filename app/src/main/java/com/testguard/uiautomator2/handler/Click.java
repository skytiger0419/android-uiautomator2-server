package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.testguard.uiautomator2.core.UiAutomatorBridge;
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
 * This handler is used to click elements in the Android UI.
 * <p>
 * Based on the element Id, click that element.
 */
public class Click extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        try {
            if (command.isElementCommand()) {
                Logger.info("Click element command");
                final AndroidElement el = command.getElement();
                el.click();
            } else {
                final Hashtable<String, Object> params = command.params();
                Point coords = new Point(Double.parseDouble(params.get("x").toString()),
                        Double.parseDouble(params.get("y").toString()));
                coords = PositionHelper.getDeviceAbsPos(coords);
//                final boolean res = Device.getInstance().getUiDevice().click(coords.x.intValue(), coords.y.intValue());
                final boolean res = UiAutomatorBridge.getInstance().getInteractionController().clickNoSync(coords.x.intValue(), coords.y.intValue());
                return getSuccessResult(res);
            }
        } catch (UiObjectNotFoundException e) {
            Logger.error("Element not found: ", e);
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
        } catch (InvalidCoordinatesException e) {
            return new AndroidCommandResult(WDStatus.INVALID_ELEMENT_COORDINATES, e);
        }
        return getSuccessResult(true);

    }
}
