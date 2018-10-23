package com.testguard.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;

import com.testguard.uiautomator2.common.exceptions.NoAttributeFoundException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.model.AndroidElement;

import org.json.JSONException;

import java.util.Hashtable;

/**
 * 获取元素属性
 * Created by Administrator on 2017/3/12.
 */

public class GetAttribute extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (command.isElementCommand()) {
            // only makes sense on an element
            final Hashtable<String, Object> params = command.params();
            try {
                final AndroidElement el = command.getElement();
                final String attr = params.get("attribute").toString();
                if ("name".equals(attr)
                        || "content_desc".equals(attr)
                        || "text".equals(attr)
                        || "className".equals(attr)
                        || "resourceId".equals(attr)
                        || "bounds".equals(attr)) {

                    return getSuccessResult(el.getStringAttribute(attr));
                } else {
                    return getSuccessResult(String.valueOf(el.getBoolAttribute(attr)));
                }
            } catch (final NoAttributeFoundException e) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT,
                        e.getMessage());
            } catch (final UiObjectNotFoundException e) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
            }
        } else {
            return getErrorResult("Unable to get attribute without an element.");
        }
    }

}
