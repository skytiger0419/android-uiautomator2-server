package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.manage.LightManage;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 亮度管理器
 * Created by Administrator on 2017/6/20.
 */
public class Light extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> params = command.params();
            String type = (String)params.get("type");
            switch (type){
                case "set":
                    if(params.containsKey("mode")){
                        int mode = (int)params.get("mode");
                        int currentMode = LightManage.getScreenMode();
                        if(mode == currentMode){
                            return getSuccessResult("current mode equal set mode");
                        }
                        LightManage.setScreenMode(mode);
                        return getSuccessResult(true);
                    }else{
                        LightManage.setScreenBrightness((int)params.get("light"));
                        return getSuccessResult(true);
                    }
                case "get":
                    if(params.containsKey("mode")){
                        return getSuccessResult(LightManage.getScreenMode());
                    }
                    return getSuccessResult(LightManage.getScreenBrightness());
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }
}
