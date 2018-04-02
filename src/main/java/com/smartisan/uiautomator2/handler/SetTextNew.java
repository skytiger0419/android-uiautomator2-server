package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.core.InteractionController;
import com.smartisan.uiautomator2.core.UiAutomatorBridge;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 输入文本
 * Created by Administrator on 2017/3/12.
 */

public class SetTextNew extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            Logger.info("device send keys");
            final Hashtable<String, Object> params = command.params();
            boolean replace = Boolean.parseBoolean(params.get("replace").toString());
            String text = params.get("text").toString();
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
