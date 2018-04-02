package com.smartisan.uiautomator2.handler;

import android.os.RemoteException;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import static com.smartisan.uiautomator2.utils.Device.getInstance;

/**
 * 锁屏
 * Created by Administrator on 2017/3/12.
 */

public class Lock extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            getInstance().getUiDevice().sleep();
            return getSuccessResult(true);
        } catch (final RemoteException e) {
            return getErrorResult("Error lock screen device");
        }
    }
}
