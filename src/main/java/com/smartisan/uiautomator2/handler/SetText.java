package com.smartisan.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.smartisan.uiautomator2.common.exceptions.ElementNotFoundException;
import com.smartisan.uiautomator2.common.exceptions.InvalidSelectorException;
import com.smartisan.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;
import com.smartisan.uiautomator2.model.KnownElements;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;
import static com.smartisan.uiautomator2.utils.Device.getInstance;

/**
 * 输入文本
 * Created by Administrator on 2017/3/12.
 */

public class SetText extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            Logger.info("send keys to element command");
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
                    return new AndroidCommandResult( WDStatus.NO_SUCH_ELEMENT);
                } catch (InvalidSelectorException e) {
                    return new AndroidCommandResult( WDStatus.INVALID_SELECTOR, e);
                }  catch ( UiAutomator2Exception | ClassNotFoundException e) {
                    return getErrorResult("Unable to find a focused element." + e.toString());
                }
            }
//            element.click();
            final Hashtable<String, Object> params = command.params();
            boolean replace = Boolean.parseBoolean(params.get("replace").toString());
            String text = params.get("text").toString();
            boolean pressEnter = false;
            if (text.endsWith("\\n")) {
                pressEnter = true;
                text = text.replace("\\n", "");
                Logger.debug("Will press enter after setting text");
            }
            boolean unicodeKeyboard = false;
            if (params.get("unicodeKeyboard") != null) {
                unicodeKeyboard = Boolean.parseBoolean(params.get("unicodeKeyboard").toString());
            }
            String currText = element.getText();
            new Clear().execute(command);
            if (!isTextFieldCleared(element)) {
                // clear could have failed, or we could have a hint in the field
                // we'll assume it is the latter
                Logger.debug("Text not cleared. Assuming remainder is hint text.");
                currText = "";
            }
            if (!replace && currText != null) {
                text = currText + text;
            }
            element.setText(text, unicodeKeyboard);
            boolean isActionPerformed;
            String actionMsg = "";
            if (pressEnter) {
                isActionPerformed = getInstance().getUiDevice().pressEnter();
                if (isActionPerformed) {
                    actionMsg = "Sent keys to the device";
                } else {
                    actionMsg = "Unable to send keys to the device";
                }
            }
            return new AndroidCommandResult( WDStatus.SUCCESS, actionMsg);
        } catch (final UiObjectNotFoundException e) {
            Logger.error("Unable to Send Keys", e);
            return new AndroidCommandResult( WDStatus.NO_SUCH_ELEMENT);
        }
    }

    private boolean isTextFieldCleared(AndroidElement element) throws UiObjectNotFoundException {
        if (element.getText() == null) {
            return true;
        } else if (element.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
