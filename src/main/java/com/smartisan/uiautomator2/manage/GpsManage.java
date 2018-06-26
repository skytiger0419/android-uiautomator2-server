package com.smartisan.uiautomator2.manage;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;

/**
 * gps定位管理,主要是打开和关闭
 * Created by Administrator on 2017/6/8.
 */
public class GpsManage {

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public static boolean isOPen() {
        Context context = Device.getInstance().getContext().getApplicationContext();
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     */
    public static void openGPS() {
        if(isOPen()){
            return;
        }
        jump2Gps();
        SystemClock.sleep(2000);
        UiObject2 gps = Device.getInstance().getUiDevice().findObject(By.clazz("android.widget.Switch"));
        gps.click();
        SystemClock.sleep(500);
        Device.getInstance().getUiDevice().pressBack();
//        Context context = Device.getInstance().getContext().getApplicationContext();
//        Intent GPSIntent = new Intent();
//        GPSIntent.setClassName("com.android.settings",
//                "com.android.settings.widget.SettingsAppWidgetProvider");
//        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
//        GPSIntent.setData(Uri.parse("custom:3"));
//        try {
//            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 强制帮用户关闭GPS
     */
    public static void closeGPS() {
        if(!isOPen()){
            return;
        }
        jump2Gps();
        SystemClock.sleep(2000);
        UiObject2 gps = Device.getInstance().getUiDevice().findObject(By.clazz("android.widget.Switch"));
        gps.click();
        SystemClock.sleep(500);
        Device.getInstance().getUiDevice().pressBack();
    }


    public static void jump2Gps() {
        Context context = Device.getInstance().getContext().getApplicationContext();
        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callGPSSettingIntent);
    }

    public static boolean isGPSEnable() {
        Context context = Device.getInstance().getContext().getApplicationContext();
        String str = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Logger.debug(str);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }

    }
}
