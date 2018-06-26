package com.smartisan.uiautomator2.manage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.smartisan.uiautomator2.utils.Device;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.invoke;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.method;

/**
 * 控制手机数据网络
 * Created by Administrator on 2017/6/8.
 */
public class DataManage {
    private static Context context = Device.getInstance().getContext();
    /**
     * 设置手机的移动数据
     */
    public static void setMobileData(boolean flag) {
        if(isLOLLIPOP()){
            setPhoneMobile(flag);
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        invoke(method(ConnectivityManager.class,"setMobileDataEnabled",boolean.class),connectivityManager, flag);
    }
    /**
     * 返回手机移动数据的状态
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState() {
        if(isLOLLIPOP()){
            return getPhoneMobile();
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (boolean)invoke(method(ConnectivityManager.class,"getMobileDataEnabled"),connectivityManager);
    }

    private static boolean getPhoneMobile(){
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (boolean)invoke(method(TelephonyManager.class,"getDataEnabled"),teleManager);
    }
    private static boolean setPhoneMobile(boolean flag){
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (boolean)invoke(method(TelephonyManager.class,"setDataEnabled",boolean.class),teleManager,flag);
    }


    private static boolean isLOLLIPOP(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }


}
