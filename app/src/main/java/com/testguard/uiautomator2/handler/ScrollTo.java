package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 * This handler is used to scroll to elements in the Android UI.
 * <p>
 * Based on the element Id of the scrollable, scroll to the object with the
 * text.
 */
public class ScrollTo extends BaseCommandHandler {


    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        try {
            String json = (String)command.params().get("text");
////            String selector = "$.params.selector", uiSelectorString, scrollToString = "";
////            uiSelectorString = JsonPath.compile(selector).read(json);
//            Device.getInstance().waitForIdle();
//            // TODO This logic needs to be changed according to the request body from the Driver
//            Matcher m = Pattern.compile("\\(\"([^)]+)\"\\)").matcher(uiSelectorString);
//            while (m.find()) {
//                scrollToString = m.group(1);
//            }
            Logger.info("Scrolled to String : ", json);
            UiObject2 uiObject2 = getInstance().getUiDevice().findObject(By.text(json));
            if(uiObject2 != null){
                return getSuccessResult(true);
            }
            Device.getInstance().scrollTo(json);
            return getSuccessResult(true);
        } catch (final UiObjectNotFoundException e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT,
                    e.getMessage());
        } catch (final NullPointerException e) { // el is null
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT,
                    e.getMessage());
        } catch (final Exception e) {
            return getErrorResult(e.getMessage());
        }

    }
}
