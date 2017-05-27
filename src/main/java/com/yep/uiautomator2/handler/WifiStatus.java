package com.yep.uiautomator2.handler;

import com.yep.uiautomator2.ServiceManage.WifiHandler;
import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

/**
 * 处理wifi链接功能
 */
public class WifiStatus extends BaseCommandHandler{

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        boolean flag = (boolean) command.params().get("flag");
        return WifiHandler.toggle(flag);
    }

}
