package com.smartisan.uiautomator2.handler;

import android.graphics.Rect;

import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取元素最最左边位置
 * Created by Administrator on 2017/3/12.
 */

public class GetLocation extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (!command.isElementCommand()) {
            return getErrorResult("Unable to get location without an element.");
        }
        try {
            final JSONObject res = new JSONObject();
            final AndroidElement el = command.getElement();
            final Rect bounds = el.getBounds();
            res.put("x", bounds.left);
            res.put("y", bounds.top);
            return getSuccessResult(res);
        } catch (final Exception e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT, e.getMessage());
        }
    }
}
