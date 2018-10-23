package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

/**
 * 按相关物理键
 * Created by Administrator on 2017/3/12.
 */

public class PressKeyCode extends BaseCommandHandler {
    public Integer keyCode;
    public Integer metaState;
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            final Hashtable<String, Object> params = command.params();
            Object kc = params.get("keycode");
            if (kc instanceof Integer) {
                keyCode = (Integer) kc;
            } else if (kc instanceof String) {
                keyCode = Integer.parseInt((String) kc);
            } else {
                throw new IllegalArgumentException("Keycode of type " + kc.getClass() + "not supported.");
            }
            if (params.get("metastate") != JSONObject.NULL) {
                metaState = (Integer) params.get("metastate");
                Device.getInstance().getUiDevice().pressKeyCode(keyCode, metaState);
            } else {
                Device.getInstance().getUiDevice().pressKeyCode(keyCode);
            }
            return getSuccessResult(true);
        } catch (final Exception e) {
            return getErrorResult(e.getMessage());
        }
    }
}
