package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.model.DeviceInfo;
import org.json.JSONException;

/**
 * 获取设备信息
 * Created by Administrator on 2017/6/28.
 */
public class DeviceInfoHandler extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        return getSuccessResult(getDeviceInfo());
//        return getSuccessResult(MTelePhoneManage.getInstance().getPhoneInfo());
    }

    private String getDeviceInfo(){
        return new DeviceInfo().toJsonString();
    }
}
