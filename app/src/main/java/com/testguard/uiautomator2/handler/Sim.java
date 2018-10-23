package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.manage.MTelePhoneManage;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

/**
 * 获取sim卡信息
 * Created by Administrator on 2017/7/12.
 */

public class Sim extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            int type = (Integer) command.params().get("type");
            if(type == 0){
                String phoneinfo = MTelePhoneManage.getInstance().getPhoneInfo();
                return getSuccessResult(phoneinfo);
            }else if(type == 1){
                String phoneinfo = MTelePhoneManage.getInstance().getPhoneinfo(type);
                return getSuccessResult(phoneinfo);
            }else {
                return getErrorResult("not support");
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }
}
