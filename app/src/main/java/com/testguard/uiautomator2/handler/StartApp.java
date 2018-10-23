package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;
import org.json.JSONException;

/**
 * 通过包名启动应用
 */
public class StartApp extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        try{
            String packageName = (String)command.params().get("packageName");
            int mode = (int)command.params().get("mode");
            Device.getInstance().startApp(packageName,mode);
            return getSuccessResult(true);
        }catch (Exception e){

        }
        return getErrorResult("params is failure");
    }
}
