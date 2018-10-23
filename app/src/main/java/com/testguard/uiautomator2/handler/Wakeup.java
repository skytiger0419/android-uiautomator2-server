package com.testguard.uiautomator2.handler;

import android.os.RemoteException;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;

import org.json.JSONException;

import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 * 唤醒屏幕
 * Created by Administrator on 2017/3/12.
 */

public class Wakeup extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            getInstance().getUiDevice().wakeUp();
            return getSuccessResult(true);
        } catch (final RemoteException e) {
            return getErrorResult("Error waking up device");
        }
    }
}
