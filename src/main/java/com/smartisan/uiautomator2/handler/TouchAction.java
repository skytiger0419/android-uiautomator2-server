package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.core.UiAutomatorBridge;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.manage.GpsManage;

import org.json.JSONException;

/**
 * touch action test
 * Created by Administrator on 2017/6/9.
 */
public class TouchAction extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        boolean flag = (boolean) command.params().get("flag");
        UiAutomatorBridge.getInstance().getInteractionController().touchDown(clickX, clickY);
        UiAutomatorBridge.getInstance().getInteractionController().touchDown(clickX, clickY);
        UiAutomatorBridge.getInstance().getInteractionController().touchUp(clickX, clickY);
        return getSuccessResult(true);
    }
}
