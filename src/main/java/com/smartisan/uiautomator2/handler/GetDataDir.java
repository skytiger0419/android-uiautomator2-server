package com.smartisan.uiautomator2.handler;

import android.os.Environment;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;


/**
 * 获取数据目录
 * Created by Administrator on 2017/3/12.
 */

public class GetDataDir extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        return getSuccessResult(Environment.getDataDirectory());
    }
}
