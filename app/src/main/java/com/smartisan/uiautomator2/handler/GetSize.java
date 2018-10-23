package com.smartisan.uiautomator2.handler;

import android.graphics.Rect;
import android.support.test.uiautomator.UiObjectNotFoundException;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取元素宽高
 * Created by Administrator on 2017/3/12.
 */

public class GetSize extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (command.isElementCommand()) {
            // Only makes sense on an element
            final JSONObject res = new JSONObject();
            try {
                final AndroidElement el = command.getElement();
                final Rect rect = el.getBounds();
                res.put("width", rect.width());
                res.put("height", rect.height());
            } catch (final UiObjectNotFoundException e) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT,
                        e.getMessage());
            } catch (final Exception e) { // handle NullPointerException
                return getErrorResult("Unknown error");
            }
            return getSuccessResult(res);
        } else {
            return getErrorResult("Unable to get text without an element.");
        }
    }
}
