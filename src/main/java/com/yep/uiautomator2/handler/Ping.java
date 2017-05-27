package com.yep.uiautomator2.handler;

import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

/**响应客户端
 * Created by Administrator on 2017/3/18.
 */

public class Ping extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        return getSuccessResult("pong");
    }
}
