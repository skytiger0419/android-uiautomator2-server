package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONException;

/**响应客户端
 * Created by Administrator on 2017/3/18.
 */

public class Ping extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if(Device.getInstance().isOffLine()){
            return getErrorResult("uiautomator is disconnected");
        }
        return getSuccessResult("pong");
    }
}
