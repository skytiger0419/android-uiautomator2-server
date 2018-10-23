package com.smartisan.uiautomator2.handler;

import android.os.RemoteException;

import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;

import org.json.JSONException;

import static com.smartisan.uiautomator2.utils.Device.getInstance;

/**
 * 打开最近app
 * Created by Administrator on 2017/3/12.
 */

public class OpenRecentApps extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            if (getInstance().getUiDevice().pressRecentApps()) {
                return getSuccessResult(true);
            } else {
                return getErrorResult("Device failed to open recentApps.");
            }
        } catch (RemoteException e) {
            return getErrorResult("Device failed to open recentApps. " + e.getMessage());
        }
    }
}
