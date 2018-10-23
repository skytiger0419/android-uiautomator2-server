package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.manage.GpsManage;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;

import org.json.JSONException;

/**
 * gps控制，打开关闭
 * Created by Administrator on 2017/6/9.
 */
public class Gps extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        boolean flag = (boolean) command.params().get("flag");
        if(flag){
            GpsManage.openGPS();
            if(!GpsManage.isOPen()){
                return getErrorResult("open gps failure");
            }
        }else{
            GpsManage.closeGPS();
            if(GpsManage.isOPen()){
                return getErrorResult("close gps failure");
            }
        }
        return getSuccessResult(true);
    }
}
