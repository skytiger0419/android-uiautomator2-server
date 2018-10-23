package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.manage.CamaraLightManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;

import java.util.Hashtable;

/**
 * 控制摄像头，当相机使用
 * Created by Administrator on 2017/6/20.
 */
public class CamaraLight extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> params = command.params();
            String type = (String)params.get("type");
            switch (type){
                case "open":
                    CamaraLightManage.getInstance().init();
                    CamaraLightManage.getInstance().open();
                    return getSuccessResult(true);
                case "close":
                    CamaraLightManage.getInstance().init();
                    CamaraLightManage.getInstance().close();
                    return getSuccessResult(true);
            }
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
        return getErrorResult("not support command type");
    }
}
