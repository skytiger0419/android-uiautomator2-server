package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * Calls the uiautomator setCompressedLayoutHierarchy() function. If set to true, ignores some views during all Accessibility operations.
 */
public class CompressedLayoutHierarchy extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {

        boolean compressLayout;
        try {
            final Hashtable<String, Object> params = command.params();
            compressLayout = (Boolean) params.get("compressLayout");
            Device.getInstance().getUiDevice().setCompressedLayoutHeirarchy(compressLayout);
        } catch (ClassCastException e) {
            return getErrorResult("must supply a 'compressLayout' boolean parameter");
        } catch (Exception e) {
            return getErrorResult("error setting compressLayoutHierarchy " + e.getMessage());
        }
        return getSuccessResult(compressLayout);
    }
}
