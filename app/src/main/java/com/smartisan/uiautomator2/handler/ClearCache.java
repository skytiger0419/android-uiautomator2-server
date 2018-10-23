package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.model.KnownElements;
import org.json.JSONException;

/**
 * 清理元素集缓存
 * Created by Administrator on 2017/4/5.
 */

public class ClearCache extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        KnownElements.getInstance().clear();
        return getSuccessResult(true);
    }
}
