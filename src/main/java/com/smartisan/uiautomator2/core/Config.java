package com.smartisan.uiautomator2.core;

import android.support.test.uiautomator.Configurator;

/**
 * 初始化时，设置默认参数值
 * Created by Administrator on 2017/6/27.
 */
public class Config {

    public static void setDefaultValue(){
        Configurator configurator = Configurator.getInstance();
        configurator.setActionAcknowledgmentTimeout(3 * 1000);        //等待click text settingt等超时时间
        configurator.setKeyInjectionDelay(200);                        //sendkey发送间隔时间
        configurator.setScrollAcknowledgmentTimeout(200);           //scroll swip 超时时间
        configurator.setWaitForIdleTimeout(5 * 1000);              //等待界面空闲超时时间，默认10秒
        configurator.setWaitForSelectorTimeout(10 * 1000);          //等待元素显示超时时间
    }
}
