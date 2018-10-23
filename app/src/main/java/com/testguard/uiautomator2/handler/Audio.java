package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.manage.MAudioManage;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 声音模式
 * Created by Administrator on 2017/6/20.
 */
public class Audio extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> params = command.params();
            String type = (String)params.get("type");
            switch (type){
                case "set":
                    int mode = (int)params.get("mode");
                    int currentMode = MAudioManage.getRingMode();
                    if(mode == currentMode){
                        return getSuccessResult("current mode equal set mode");
                    }
                    MAudioManage.setRingMode(mode);
                    return getSuccessResult(true);
                case "get":
                    int ringMode = MAudioManage.getRingMode();
                    return getSuccessResult(ringMode);
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }
}
