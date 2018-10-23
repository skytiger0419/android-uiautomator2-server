package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 手指放大缩小图片及其他
 * Created by Administrator on 2017/3/12.
 */

public class Pinch extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        final Hashtable<String, Object> params = command.params();
        final String direction = params.get("direction").toString();
        final Integer percent = (Integer) params.get("percent");
        final Integer steps = (Integer) params.get("steps");
        AndroidElement element;
        try {
            element = command.getElement();
            if (element == null) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
            }
        } catch (final Exception e) { // JSONException, NullPointerException, etc.
            return getErrorResult("Unknown error:" + e.getMessage());
        }
        Logger.debug("Pinching " + direction + " " + percent.toString() + "%"
                + " with steps: " + steps.toString());
        Object el = element.getUiObject();
        boolean res = true;
        if (direction.equals("in")) {
            if(el instanceof UiObject2){

                int percent_2 = (percent < 0) ? 0 : (percent > 100) ? 100 : percent;
                float percentage = percent_2 / 100f;
                ((UiObject2) el).pinchOpen(percentage);
                res = true;
            }
            if(el instanceof UiObject){
                try {
                    res = ((UiObject) el).pinchIn(percent,steps);
                } catch (final UiObjectNotFoundException e) {
                    return getErrorResult("Selector could not be matched to any UI element displayed");
                }
            }
        } else {
            if(el instanceof UiObject2){
                int percent_2 = (percent < 0) ? 0 : (percent > 100) ? 100 : percent;
                float percentage = percent_2 / 100f;
                ((UiObject2) el).pinchClose(percentage);
                res = true;
            }
            if(el instanceof UiObject){
                try {
                    res = ((UiObject) el).pinchOut(percent,steps);
                } catch (final UiObjectNotFoundException e) {
                    return getErrorResult("Selector could not be matched to any UI element displayed");
                }
            }
        }
        if (res) {
            return getSuccessResult(res);
        } else {
            return getErrorResult("Pinch did not complete successfully");
        }
    }
}
