package com.smartisan.uiautomator2.handler;

import android.content.Context;
import android.content.Intent;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.utils.Device;

import org.json.JSONException;

/**
 * 加载相关setting设置界面
 * Created by Administrator on 2017/6/13.
 */
public class StartSettingAction extends BaseCommandHandler{
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            String action = (String)command.params().get("action");
            Context context = Device.getInstance().getContext().getApplicationContext();
            Intent actionIntent = new Intent(action);
            actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(actionIntent);
            return getSuccessResult(true);
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }

    }
}
