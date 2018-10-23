package com.testguard.uiautomator2.test;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.SdkSuppress;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.testguard.uiautomator2.common.exceptions.SessionRemovedException;
import com.testguard.uiautomator2.server.ServerInstrumentation;
import com.testguard.uiautomator2.utils.Logger;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 19)
public class UiAutomator2Server {
    private static ServerInstrumentation serverInstrumentation;
    private static int serverPort = 4724;
    private Context ctx;

    /**
     * Starts the server on the device
     */
    @Test
    public void startServer() throws InterruptedException {
        if (serverInstrumentation == null) {
            ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            serverInstrumentation = ServerInstrumentation.getInstance(ctx, serverPort);
            Logger.info("Starting Server");
//            PermissionManage.checkPermission(); //检查应用申请权限是否已开通
            try {
                while (!serverInstrumentation.isStopServer()) {
                    SystemClock.sleep(500);
                    serverInstrumentation.startServer();
                }
            }catch (SessionRemovedException e){
                //Ignoring SessionRemovedException
            }
        }
    }

}