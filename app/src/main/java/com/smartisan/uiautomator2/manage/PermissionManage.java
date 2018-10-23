package com.smartisan.uiautomator2.manage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import com.smartisan.uiautomator2.utils.Device;

/**
 *检查应用权限是否都获取
 * Created by Administrator on 2017/8/22.
 */
public class PermissionManage {
    private static int CODE_FOR_WRITE_PERMISSION = 101;
    private static String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
//            Manifest.permission.CHANGE_CONFIGURATION,
//            Manifest.permission.SET_TIME_ZONE
//            Manifest.permission.WRITE_SETTINGS,
    };

    /**
     * 检查app权限
     */
    public static void checkPermission(){
        Context context = Device.getInstance().getContext();
        boolean flag = false;
        for(String tmp: permissions){
            int hasWritePermission = ActivityCompat.checkSelfPermission(context, tmp);
            if(hasWritePermission != PackageManager.PERMISSION_GRANTED){
                flag = true;
            }
        }
        //跳转详情
        if(flag){
            jumpAppDetail(context);
        }
    }

    /**
     * 跳到应用详情
     * @param context
     */
    private static void jumpAppDetail(Context context){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }


}
