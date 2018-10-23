package com.testguard.uiautomator2.handler;

import android.os.RemoteException;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;

import org.json.JSONException;

import java.util.Hashtable;

import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 *
 *  This handler is used to freep rotation in th, click that element.
 */
public class Rotation extends BaseCommandHandler {


    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        final Hashtable<String, Object> params = command.params();
        String status = (String)params.get("status");
        try {
            if(status.equalsIgnoreCase("freeze")){
                getInstance().getUiDevice().freezeRotation();
            }else if(status.equalsIgnoreCase("unfreeze")){
                getInstance().getUiDevice().unfreezeRotation();
            }else{
                return getErrorResult("not support ratation to " + status);
            }
            return getSuccessResult(true);
        } catch (final RemoteException e) {
            return getErrorResult("Error ratation to " + status);
        }

    }
}
