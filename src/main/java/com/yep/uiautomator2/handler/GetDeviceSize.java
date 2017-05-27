package com.yep.uiautomator2.handler;

import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import org.json.JSONObject;
import static com.yep.uiautomator2.utils.Device.getInstance;

/**
 * 获取屏幕大小
 * Created by Administrator on 2017/3/12.
 */

public class GetDeviceSize extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (!command.isElementCommand()) {
            // only makes sense on a device
            final JSONObject res = new JSONObject();
            try {
                res.put("height", getInstance().getUiDevice().getDisplayHeight());
                res.put("width", getInstance().getUiDevice().getDisplayWidth());
            } catch (final JSONException e) {
                getErrorResult("Error serializing height/width data into JSON");
            }
            return getSuccessResult(res);
        } else {
            return getErrorResult("Unable to get device size on an element.");
        }
    }
}
