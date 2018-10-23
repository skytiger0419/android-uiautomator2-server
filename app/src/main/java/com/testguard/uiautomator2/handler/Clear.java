package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.common.exceptions.ElementNotFoundException;
import com.testguard.uiautomator2.common.exceptions.InvalidSelectorException;
import com.testguard.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.model.KnownElements;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;


/**
 * 清理文本内容
 * Created by Administrator on 2017/3/12.
 */

public class Clear extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            Logger.info("Clear element command");
            AndroidElement element;
            if (command.isElementCommand()) {
                element = command.getElement();
                if (element == null) {
                    return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
                }
            } else {
                //perform action on focused element
                try {
                    element = KnownElements.getInstance().getElement(android.support.test.uiautomator.By.focused(true), null /* by */);
                } catch (ElementNotFoundException e) {
                    return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
                } catch (InvalidSelectorException e) {
                    return new AndroidCommandResult(WDStatus.INVALID_SELECTOR, e);
                } catch (UiAutomator2Exception | ClassNotFoundException e) {
                    return getErrorResult("Unable to find a focused element." + e.toString());
                }
            }
            element.clear();
        } catch (UiObjectNotFoundException e) {
            Logger.error("Element not found: ", e);
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
        }
        return getSuccessResult("Element Cleared");
    }
}
