//package com.yep.uiautomator2.test;
//
//import android.os.SystemClock;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.test.filters.SdkSuppress;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import com.yep.uiautomator2.server.AndroidServer;
//import com.yep.uiautomator2.utils.Device;
//import com.yep.uiautomator2.utils.Logger;
//import fi.iki.elonen.NanoHTTPD;
//
//@RunWith(AndroidJUnit4.class)
//@SdkSuppress(minSdkVersion = 19)
//public class YepUiAutomator2Server1 {
//    private static AndroidServer androidServer;
//    private static int serverPort = 4724;
//
//
//    @Before
//    public void setUp() throws Exception {
//        Device.getInstance().getUiDevice().wakeUp();
//        if (androidServer == null) {
//            androidServer = new AndroidServer(serverPort);
//            androidServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
//        }
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        if (androidServer != null) {
//            androidServer.stop();
//        }
//    }
//
//    /**
//     * Starts the server on the device
//     */
//    @Test
//    public void startServer() throws InterruptedException {
//        Logger.info("Starting Server on port " + serverPort);
//        while (androidServer.isAlive()) {
//            SystemClock.sleep(200);
//            if(Device.getInstance().isOffLine()){
//                break;
//            }
//        }
//    }
//
//}