package com.yep.uiautomator2.server;

import android.content.Context;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import com.yep.uiautomator2.utils.Device;
import com.yep.uiautomator2.utils.Logger;
import java.io.IOException;

/**
 * 框架主服务
 */
public class ServerInstrumentation {
    private static ServerInstrumentation instance;
    private Context context;
    private int serverPort;
    private Looper looper;
    private AndroidServer androidServer;
    private PowerManager.WakeLock wakeLock;

    private ServerInstrumentation(Context ctx, int serverPort) {
        if (!isValidPort(serverPort)) {
            throw new RuntimeException(("Invalid port: " + serverPort));
        }
        this.serverPort = serverPort;
        this.context = ctx;
        androidServer = new AndroidServer(serverPort);
    }

    public boolean isStopServer() {
        if (androidServer != null) {
            return androidServer.isAlive();
        }
        return false;
    }

    private boolean isValidPort(int port) {
        return port >= 1024 && port <= 65535;
    }

    public static synchronized ServerInstrumentation getInstance(Context ctx, int serverPort) {
        if (instance == null) {
            instance = new ServerInstrumentation(ctx,serverPort);
        }
        return instance;
    }

    public void startServer() throws InterruptedException {
        if (androidServer != null && androidServer.isAlive()) {
            return;
        }
        startAndroidServer();
    }

    private void startAndroidServer() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "HQ_U2");
        Looper.prepare();
        looper = Looper.myLooper();
        try {
            wakeLock.acquire();
            Device.getInstance().getUiDevice().wakeUp();
            androidServer.start();
        } catch (RemoteException e) {
            Logger.error("Remote Exception while waking up", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }

    public void stopServer() {
        try {
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                } catch (Exception e) {/* ignore */}
                wakeLock = null;
            }
            stopLooping();
            androidServer.stop();
        } finally {
            instance = null;
        }
    }

    private void stopLooping() {
        if (looper == null) {
            return;
        }
        looper.quit();
    }
}
