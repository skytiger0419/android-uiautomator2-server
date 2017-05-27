package com.yep.uiautomator2.utils;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import com.yep.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.yep.uiautomator2.model.AndroidElement;
import com.yep.uiautomator2.model.By;
import com.yep.uiautomator2.model.UiObject2Element;
import com.yep.uiautomator2.model.UiObjectElement;

/**
 * 设备操作类
 */
public class Device {

    private static Device instance;
    private Context context;
    private UiAutomation mUiAutomator;
    private UiDevice mUiDevice;

    private Device(){
        context =  InstrumentationRegistry.getInstrumentation().getContext();
        mUiAutomator = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static final Device getInstance() {
        if(instance == null){
            instance = new Device();
        }
        return instance;
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


}
