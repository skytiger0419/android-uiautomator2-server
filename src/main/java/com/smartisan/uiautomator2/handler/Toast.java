package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.model.NotificationListener;
import org.json.JSONException;

import java.util.Hashtable;
import java.util.List;

/**
 * 监听toast
 */
public class Toast extends BaseCommandHandler {


    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        final Hashtable<String, Object> params = command.params();
        final boolean flag = (boolean) params.get("flag");
        if (flag) {
            NotificationListener.getInstance().start();
            return getSuccessResult(true);
        } else {
            NotificationListener.getInstance().stop();
            List<CharSequence> toastMsg = NotificationListener.getInstance().getToastMSGs();
            StringBuilder sb = new StringBuilder();
            for(CharSequence tmp:toastMsg){
                sb.append(tmp.toString());
            }
            return getSuccessResult(sb.toString());
        }
    }
}
