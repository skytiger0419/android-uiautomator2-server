package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import static com.testguard.uiautomator2.utils.API.API_18;

/** 打开通知栏
 * Created by Administrator on 2017/3/12.
 */

public class OpenNotification extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        // method was only introduced in API Level 18
        Logger.info("Opened Notification");
        boolean isNotificationOpened;
        if (!API_18) {
            return getErrorResult("Unable to open notifications on device below API level 18");
        }
        isNotificationOpened = Device.getInstance().getUiDevice().openNotification();
        if (isNotificationOpened) {
            return getSuccessResult(true);
        } else {
            return getErrorResult("Device failed to open notifications.");
        }
    }
}
