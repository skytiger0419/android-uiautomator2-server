package com.testguard.uiautomator2.handler;

import android.os.Environment;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONException;

import java.util.Hashtable;

/**
 * 获取设备路径,临时路径，sd卡路径，外置sd卡路径
 * Created by thomas.ning on 2017/7/10.
 */

public class DevicePath extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> paramas = command.params();
            int mode = (Integer)paramas.get("mode");
            String result = null;
            switch(mode){
                case 0: //临时数据目录
                    result = "/data/local/tmp";
                    break;
                case 1: //内置sd卡目录
                    result = Environment.getExternalStorageDirectory().getAbsolutePath();
                    break;
                case 2: //外置sd卡目录
                    result = Device.getInstance().getExtStoragePath(true);
                    break;
            }
            return  getSuccessResult(result);
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }
}
