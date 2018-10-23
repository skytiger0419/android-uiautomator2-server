package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObject2;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.model.KnownElements;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;

/**
 * 获取父级元素
 * Created by Administrator on 2017/3/12.
 */
public class GetParent extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (command.isElementCommand()) {
            try {
                final AndroidElement element = command.getElement();
                Object el = element.getUiObject();
                if (el instanceof UiObject2) {
                    String id = UUID.randomUUID().toString();
                    UiObject2 u2 = ((UiObject2) el).getParent();
                    addElement(id, u2);
                    JSONObject result = new JSONObject();
                    result.put("ELEMENT", id);
                    return getSuccessResult(result);
                } else {
                    getErrorResult("Unable to get parent on an u1 element.");
                }
            } catch (final Exception e) { // handle NullPointerException
                return getErrorResult("Unknown error");
            }
        }
        return getErrorResult("Unable to get parent without an element.");
    }

    private void addElement(String id, UiObject2 element) {
        AndroidElement androidElement = Device.getInstance().getAndroidElement(id, element, null);
        KnownElements.getInstance().add(androidElement);
    }

}
