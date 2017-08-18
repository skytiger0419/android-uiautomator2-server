package com.yep.uiautomator2.handler;

import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import com.yep.uiautomator2.model.KnownElements;
import com.yep.uiautomator2.utils.ReflectionUtils;

import org.json.JSONException;

/**
 * 清理界面缓存区数据
 * Created by Administrator on 2017/4/5.
 */

public class ClearUiCache extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        ReflectionUtils.clearAccessibilityCache();
        return getSuccessResult(true);
    }
}
