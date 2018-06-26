package com.smartisan.uiautomator2.manage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * 剪切板管理
 */
public class ClipboardManage {
    private static final int DEFAULT_LABEL_LEN = 10;
    private final Context context;

    public ClipboardManage(Context context) {
        this.context = context;
    }

    private ClipboardManager getManager() {
        final ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null) {
            throw new ClipboardError("Cannot receive ClipboardManager instance from the system");
        }
        return cm;
    }

    public String getTextData() {
        final ClipboardManager cm = getManager();
        if (!cm.hasPrimaryClip()) {
            return "";
        }
        final ClipData cd = cm.getPrimaryClip();
        if (cd == null || cd.getItemCount() == 0) {
            return "";
        }
        final CharSequence text = cd.getItemAt(0).coerceToText(context);
        return text == null ? "" : text.toString();
    }

    public void setTextData(@Nullable String label, String data) {
        final ClipboardManager cm = getManager();
        if (label == null) {
            label = data.length() >= DEFAULT_LABEL_LEN
                    ? data.substring(0, DEFAULT_LABEL_LEN)
                    : data;
        }
        cm.setPrimaryClip(ClipData.newPlainText(label, data));
    }

    public static class ClipboardError extends RuntimeException {
        ClipboardError(String message) {
            super(message);
        }
    }

    public enum ClipDataType {
        PLAINTEXT;
        public static String supportedDataTypes() {
            return Arrays.toString(values());
        }
    }
}
