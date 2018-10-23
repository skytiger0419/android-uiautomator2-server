package com.smartisan.uiautomator2.manage;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

import com.smartisan.uiautomator2.utils.Device;

/**
 * 亮度管理
 * Created by Administrator on 2017/6/20.
 */
public class LightManage {

    public static Context context = Device.getInstance().getContext();
    /**
     * 获得当前屏幕亮度的模式
     *
     * @return 1 为自动调节屏幕亮度,0 为手动调节屏幕亮度,-1 获取失败
     */
    public static int getScreenMode() {
        int mode = -1;
        try {
            mode = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

    /**
     * 获得当前屏幕亮度值
     *
     * @return 0--255
     */
    public static int getScreenBrightness() {
        int screenBrightness = -1;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }
    /**
     * 设置当前屏幕亮度的模式
     *
     * @param mode 1 为自动调节屏幕亮度,0 为手动调节屏幕亮度
     */
    public static void setScreenMode(int mode) {
        try {
            Uri uri = Settings.System
                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
            context.getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     *
     * @param paramInt 0-255
     */
    public static void setScreenBrightness(int paramInt) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, paramInt);
        Uri uri = Settings.System
                .getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        context.getContentResolver().notifyChange(uri, null);
    }
}
