package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiWatcher;
import android.text.TextUtils;

import com.testguard.uiautomator2.common.monitor.UiWatchers2;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册卸载监控方案
 * Created by Administrator on 2017/7/13.
 */
public class UiWatcherHandler extends BaseCommandHandler {
    private UiDevice uiDevice = Device.getInstance().getUiDevice();

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            String flag = (String) command.params().get("flag");
            String name = (String) command.params().get("name");
            switch (flag){
                case "add":
                    String packageName = null;
                    String text = (String) command.params().get("text");
                    Object obj = command.params().get("packageName");
                    if(obj != null && obj != JSONObject.NULL){
                        packageName = (String)command.params().get("packageName");
                    }
                    UiWatchers2.getInstance().add(name, makeWatcher(packageName,text));
                    break;
                case "remove":
                    UiWatchers2.getInstance().remove(name);
                    break;
                case "removeall":
                    UiWatchers2.getInstance().clearAll();
                    break;
                default:
                    return getErrorResult("not support " + flag);
            }
            return getSuccessResult(true);
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }


    private UiWatcher makeWatcher(final String packageName, final String text){
        return new UiWatcher() {
            @Override
            public boolean checkForCondition() {
                return postHandler(packageName,text);
            }
        };
    }

    /**
     * 处理监控事件
     * @param text
     */
    private boolean postHandler(String packageName,String text){
        UiObject2 uiObject2 = uiDevice.findObject(By.text(text));
        if(uiObject2 != null){
            if(uiObject2.isEnabled()){
                if(TextUtils.isEmpty(packageName)){
                    try{
                        uiObject2.click();
                        return true;
                    }catch (Exception e){
                        Logger.warning(e);
                    }
                }else{
                    if(!TextUtils.isEmpty(uiObject2.getApplicationPackage())
                            && uiObject2.getApplicationPackage().equalsIgnoreCase(packageName)){
                        try{
                            uiObject2.click();
                            return true;
                        }catch (Exception e){
                            Logger.warning(e);
                        }
                    }
                }

            }
        }
        return false;
    }
}
