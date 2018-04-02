package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.utils.Device;

import org.json.JSONException;

/**
 * 按后退键
 * Created by Administrator on 2017/3/12.
 */

public class PressBack extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        Device.getInstance().back();
        // Press back returns false even when back was successfully pressed.
        // Always return true.
        return getSuccessResult(true);
    }
}
