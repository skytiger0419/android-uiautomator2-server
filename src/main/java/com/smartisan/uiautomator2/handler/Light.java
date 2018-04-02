package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.ServiceManage.LightManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
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
                        int light = (int)params.get("light");
                        LightManage.setScreenBrightness(light);
                        return getSuccessResult(true);
                    }
                case "get":
                    if(params.containsKey("mode")){
                        int mode = LightManage.getScreenMode();
                        return getSuccessResult(mode);
                    }
                    int light = LightManage.getScreenBrightness();
                    return getSuccessResult(light);
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }
}
