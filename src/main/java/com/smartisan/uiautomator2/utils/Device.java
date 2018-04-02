package com.smartisan.uiautomator2.utils;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.smartisan.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.smartisan.uiautomator2.model.AndroidElement;
import com.smartisan.uiautomator2.model.By;
import com.smartisan.uiautomator2.model.UiObject2Element;
import com.smartisan.uiautomator2.model.UiObjectElement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import static com.smartisan.uiautomator2.utils.ReflectionUtils.invoke;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.method;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.getField;

/**
 * 设备操作类
 */
public class Device {

    private static Device instance;
    private Context context;
    private UiAutomation mUiAutomator;
    private UiDevice mUiDevice;

    private Device(){
        context =  InstrumentationRegistry.getInstrumentation().getTargetContext();
        mUiAutomator = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static final Device getInstance() {
        if(instance == null){
            instance = new Device();
        }
        return instance;
    }

    public boolean isOffLine(){
        return (Integer)getField(UiAutomation.class, "mConnectionId", mUiAutomator) == -1;
    }

    public Context getContext(){
        return context;
    }

    public UiDevice getUiDevice(){
        return mUiDevice;
    }

    public AndroidElement getAndroidElement(String id, Object element, By by) throws UiAutomator2Exception {
        if (element instanceof UiObject2) {
            return new UiObject2Element(id, (UiObject2) element, by);
        } else if (element instanceof UiObject) {
            return new UiObjectElement(id, (UiObject) element, by);
        } else {
            throw new UiAutomator2Exception("Unknown Element type: " + element.getClass().getName());
        }
    }

    public void wake() throws RemoteException {
        mUiDevice.wakeUp();
    }

    public void startApp(String packageName,int mode){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(mode == 0){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

    public boolean back() {
        return mUiDevice.pressBack();
    }

    public void scrollTo(String scrollToString) throws UiObjectNotFoundException {
        // TODO This logic needs to be changed according to the request body from the Driver
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().descriptionContains(scrollToString).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().textContains(scrollToString).instance(0));
    }

    /**
     * 获取手机序列号
     * @return
     */
    public String getSerialNum(){
        return getProp("ro.serialno");
    }

    /**
     * 获取软件版本
     * @return
     */
    public String getIMEI(){
        return getProp("ro.ril.oem.imei");
    }

    /**
     * 获取软件版本
     * @return
     */
    public String getSwVersion(){
        return getProp("ro.build.display.id");
    }
    //获取手机相关属性值
    public String getProp(String key){
        Object obj = invoke(method("android.os.SystemProperties","get",String.class),null, key);
        if(obj == null){
            return null;
        }
        return (String)obj;
    }


    /**
     * 获取外置sd卡目录
     * @param is_removale
     * @return
     */
    public String getExtStoragePath( boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取运行内存大小
     * @return
     */
    public double getMemSize(){
        int totalMem = 0;
        try {
            String memTotal = mUiDevice.executeShellCommand("cat /proc/meminfo");
            for(String tmp : memTotal.split("\n")){
                String mem = tmp.split("\\s+")[1];
                return Math.ceil(Double.parseDouble(mem)/(1024*1024));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMem;
    }

}
