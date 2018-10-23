package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.manage.BTManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

/**
 * 控制蓝牙相关项
 * Created by Administrator on 2017/6/9.
 */
public class Bluetooth extends BaseCommandHandler{
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        boolean flag = (boolean) command.params().get("flag");
        if(!BTManage.isBluetoothSupported()){
            return getErrorResult("not support bluetooth");
        }
        boolean result = false;
        if(flag){
            result = BTManage.openBluetooth();
            if(!result){
                return getErrorResult("open bluetoosh failure");
            }
        }else{
            result = BTManage.closeBluetooth();
            if(!result){
                return getErrorResult("close bluetoosh failure");
            }
        }
        return getSuccessResult(true);
    }
}
