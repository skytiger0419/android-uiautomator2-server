package com.smartisan.uiautomator2.handler;

import android.util.Base64;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.manage.ClipboardManage;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

/**
 * Clipbord
 */
public class Clipbord extends BaseCommandHandler {

    private static String fromBase64String(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        Logger.info("Set Clipboard command");
        ClipboardManage.ClipDataType contentType = ClipboardManage.ClipDataType.PLAINTEXT;
        String label = null;
        String content = null;
        final Hashtable<String, Object> params = command.params();
        try {
            if(params.contains("contentType")){
                contentType = ClipboardManage.ClipDataType.valueOf(params
                        .get("contentType").toString()
                        .toUpperCase());
            }
            if(params.contains("content")){
                content = fromBase64String(params.get("content").toString());
            }
            if(params.contains("label")){
                label = params.get("label").toString();
            }
            Device.getInstance().getInstrumentation().runOnMainSync(new ClipboardRunnable(contentType, label, content) );
            return getSuccessResult(true);
        } catch (final Exception e) {
            Logger.error(String.format("Only '%s' content types are supported. '%s' is given instead",
                    ClipboardManage.ClipDataType.supportedDataTypes(),
                    contentType), e);
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR);
        }
    }

    // Clip feature should run with main thread
    private class ClipboardRunnable implements Runnable {
        private ClipboardManage.ClipDataType contentType;
        private String label;
        private String content;

        ClipboardRunnable(ClipboardManage.ClipDataType contentType, String label, String content) {
            this.contentType = contentType;
            this.label = label;
            this.content = content;
        }

        @Override
        public void run() {
            switch (contentType) {
                case PLAINTEXT:
                    new ClipboardManage(Device.getInstance().getContext()).setTextData(label, content);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
