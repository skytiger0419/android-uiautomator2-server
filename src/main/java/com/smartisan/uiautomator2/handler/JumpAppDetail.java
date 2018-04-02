package com.smartisan.uiautomator2.handler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.utils.Device;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 跳转应用详情
 * Created by Administrator on 2017/3/12.
 */

public class JumpAppDetail extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
            final Hashtable<String, Object> params = command.params();
            final String packageName = (String) params.get("package");
            jumpAppDetail(packageName);
            return getSuccessResult("jump to App detail " + packageName);
    }

    /**
     * 跳到应用详情
     * @param packageName
     */
    private static void jumpAppDetail(String packageName){
        Context context = Device.getInstance().getContext();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",packageName, null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
