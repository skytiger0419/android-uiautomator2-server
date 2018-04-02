package com.smartisan.uiautomator2.model;

import android.support.test.uiautomator.BySelector;
import com.smartisan.uiautomator2.common.exceptions.ElementNotFoundException;
import com.smartisan.uiautomator2.common.exceptions.InvalidSelectorException;
import com.smartisan.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.smartisan.uiautomator2.model.internal.CustomUiDevice;
import com.smartisan.uiautomator2.utils.Device;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class KnownElements {
    private static ConcurrentHashMap<String, AndroidElement> cache;   //缓存目前已知元素
    private static KnownElements instance = null;

    private KnownElements(){
        cache =  new ConcurrentHashMap<String, AndroidElement>();
    }

    public static KnownElements getInstance(){
        if(instance == null){
            instance = new KnownElements();
        }
        return instance;
    }


    private String getCacheKey(AndroidElement element) {
        for (Map.Entry<String, AndroidElement> entry : cache.entrySet()) {
            if (entry.getValue().equals(element)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getIdOfElement(AndroidElement element) {
        if (cache.containsValue(element)) {
            return getCacheKey(element);
        }
        return null;
    }

    public  AndroidElement getElementFromCache(String id) {
//        Logger.error("缓存大小：" + cache.size());
        return cache.get(id);
    }

    /**
     *
     * @param ui2BySelector, for finding {@link android.support.test.uiautomator.UiObject2} element derived using {@link By}
     * @param by, user provided selector criteria from appium client.
     * @return
     */
    public AndroidElement getElement(final BySelector ui2BySelector, By by)
            throws ElementNotFoundException, InvalidSelectorException,
            UiAutomator2Exception, ClassNotFoundException {
        Object ui2Object = CustomUiDevice.getInstance().findObject(ui2BySelector);
        if (ui2Object == null) {
            throw new ElementNotFoundException();
        }
        String id = UUID.randomUUID().toString();
        AndroidElement androidElement = Device.getInstance().getAndroidElement(id, ui2Object, by);
        cache.put(androidElement.getId(), androidElement);
        return androidElement;
    }

    public String add(AndroidElement element) {
        if (cache.containsValue(element)) {
            return getCacheKey(element);
    }
        cache.put(element.getId(), element);
        return element.getId();
    }

    public void clear() {
        if (!cache.isEmpty()) {
            cache.clear();
            System.gc();
        }

    }
}
