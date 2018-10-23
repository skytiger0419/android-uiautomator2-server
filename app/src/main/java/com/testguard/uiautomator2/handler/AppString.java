package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * This handler is used to update the apk strings.
 */
public class AppString extends BaseCommandHandler {
    private static String jsonPath = "/data/local/tmp/strings.json";

    @Override
    public AndroidCommandResult execute(final AndroidCommand command) {
        if (!loadStringsJson()) {
            return getErrorResult("Unable to load json file and update strings.");
        }
        return getSuccessResult(true);
    }

    public static boolean loadStringsJson() {
        Logger.debug("Loading json...");
        try {
            final File jsonFile = new File(jsonPath);
            // json will not exist for apks that are only on device
            // because the node server can't extract the json from the apk.
            if (!jsonFile.exists()) {
                return false;
            }
            final DataInputStream dataInput = new DataInputStream(
                    new FileInputStream(jsonFile));
            final byte[] jsonBytes = new byte[(int) jsonFile.length()];
            dataInput.readFully(jsonBytes);
            // this closes FileInputStream
            dataInput.close();
            final String jsonString = new String(jsonBytes, "UTF-8");
            Find.apkStrings = new JSONObject(jsonString);
            Logger.debug("json loading complete.");
        } catch (final Exception e) {
            Logger.error("Error loading json: " + e.getMessage());
            return false;
        }
        return true;
    }
}