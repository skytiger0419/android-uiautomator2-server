package com.testguard.uiautomator2.handler;

import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import com.testguard.uiautomator2.core.InteractionController;
import com.testguard.uiautomator2.core.UiAutomatorBridge;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * 长按物理键
 * Created by Administrator on 2017/3/12.
 */
public class LongPressKeyCode extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        InteractionController interactionController = UiAutomatorBridge.getInstance().getInteractionController();
        try {
            final Hashtable<String, Object> params = command.params();
            Integer keyCode = (Integer) params.get("keycode");
            Integer metaState = params.get("metastate") != JSONObject.NULL ? (Integer) params
                    .get("metastate") : 0;
            Integer duration = params.get("duration") != JSONObject.NULL ? (Integer) params
                    .get("duration") : 0;
            final long eventTime = SystemClock.uptimeMillis();
            // Send an initial down event
            final KeyEvent downEvent = new KeyEvent(eventTime, eventTime,
                    KeyEvent.ACTION_DOWN, keyCode, 0, metaState,
                    KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD);
            if (interactionController.injectEventSync(downEvent)) {
                // Send a repeat event. This will cause the FLAG_LONG_PRESS to be set.
                final KeyEvent repeatEvent = KeyEvent.changeTimeRepeat(downEvent,
                        eventTime, 1);
                interactionController.injectEventSync(repeatEvent);
                Thread.sleep(duration * 1000);
                // Finally, send the up event
                final KeyEvent upEvent = new KeyEvent(eventTime, eventTime,
                        KeyEvent.ACTION_UP, keyCode, 0, metaState,
                        KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD);
                interactionController.injectEventSync(upEvent);
            }
            return getSuccessResult(true);

        } catch (final Exception e) {
            return getErrorResult(e.getMessage());
        }
    }
}
