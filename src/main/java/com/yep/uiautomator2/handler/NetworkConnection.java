package com.yep.uiautomator2.handler;

import com.yep.uiautomator2.ServiceManage.*;
import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import com.yep.uiautomator2.model.NetworkConnectionEnum;
import org.json.JSONException;

/**
 * 调整wifi状态
 */
public class NetworkConnection extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        int requestedType = Integer.parseInt(command.params().get("type").toString());
        NetworkConnectionEnum networkType = NetworkConnectionEnum.getNetwork(requestedType);
        switch (networkType) {
            case WIFI:
                return WifiHandler.toggle(true);
            case DATA:
            case AIRPLANE:
            case ALL:
            case NONE:
                return getErrorResult("Setting Network Connection to: " + networkType.getNetworkType() + " :is not implemented");
            default:
                return getErrorResult("Invalid Network Connection type: "+ requestedType);
        }
    }
}
