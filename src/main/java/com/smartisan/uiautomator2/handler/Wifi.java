package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.ServiceManage.WifiManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.model.SwitchStatus;
import org.json.JSONException;

/**
 * 处理wifi链接功能
 */
public class Wifi extends BaseCommandHandler{

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            String type = (String) command.params().get("type");
            switch (type){
                case "set":
                    boolean flag = (boolean) command.params().get("flag");
                    SwitchStatus status =  WifiManage.getInstance().toggle(flag);
                    if(status.code == 0){
                        return getSuccessResult(true);
                    }else{
                        return getErrorResult(status.msg);
                    }
                case "get_info":
                    return getSuccessResult(WifiManage.getInstance().getWifiInfo());
                case "get_status":
                    return getSuccessResult(WifiManage.getInstance().getWifiStatus());
                case "connect":
                    String hot = (String) command.params().get("hot");
                    String password = (String) command.params().get("password");
                    int ctype = (int)command.params().get("ctype");
                    WifiManage.getInstance().connect(hot,password,ctype);
                    return getSuccessResult(true);
                case "disconnect":
                    WifiManage.getInstance().disconnect();
                    return getSuccessResult(true);
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }


}
