package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.core.InteractionController;
import com.testguard.uiautomator2.core.UiAutomatorBridge;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * send key by InteractionController
 */
public class SendKeys extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            Logger.info("device send keys");
            final Hashtable<String, Object> params = command.params();
            boolean replace = Boolean.parseBoolean(params.get("replace").toString());
            String text = params.get("text").toString();
            if(replace){
                new Clear().execute(command);
            }
            // Send the delete key to clear the existing text, then send the new text
            InteractionController ic = UiAutomatorBridge.getInstance().getInteractionController();
            ic.sendText(text);
            return getSuccessResult(true);
        } catch (final Exception e) {
            Logger.error("Unable to Send Keys", e);
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR);
        }
    }
}
