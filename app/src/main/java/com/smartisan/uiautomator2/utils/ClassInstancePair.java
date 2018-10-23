package com.smartisan.uiautomator2.utils;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Simple class for holding a String 2-tuple. An android class, and instance number, used for
 * finding elements by xpath.
 */
public class ClassInstancePair {

    private String androidClass;
    private String instance;

    public ClassInstancePair(String clazz, String inst) {
        androidClass = clazz;
        instance = inst;
    }

    public String getAndroidClass() {
        return androidClass;
    }

    public String getInstance() {
        return instance;
    }

    public BySelector getSelector() {
        String androidClass = getAndroidClass();

        //TODO: remove below comments once code get reviewed
        //below commented line related to UiAutomator V1(bootstrap) version, as we don't have possibility
        // in V2 version to use instance, so directly returning By.clazz
        // new UiSelector().className(androidClass).instance(Integer.parseInt(instance));
        return By.clazz(androidClass);
    }


}
