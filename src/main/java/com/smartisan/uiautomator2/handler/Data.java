package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.manage.DataManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

import java.util.Hashtable;

/**
 * 控制数据开关
 * Created by Administrator on 2017/6/20.
 */
public class Data extends BaseCommandHandler{
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> params = command.params();
            String type = (String)params.get("type");
            switch (type){
                case "open":
                    if(DataManage.getMobileDataState()){
                        return getSuccessResult("current status equal expect status");
                    }
                    DataManage.setMobileData(true);
                    return getSuccessResult(true);
                case "close":
                    if(!DataManage.getMobileDataState()){
                        return getSuccessResult("current status equal expect status");
                    }
                    DataManage.setMobileData(false);
                    return getSuccessResult(true);
                case "get":
                    return getSuccessResult(DataManage.getMobileDataState());
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }
}
