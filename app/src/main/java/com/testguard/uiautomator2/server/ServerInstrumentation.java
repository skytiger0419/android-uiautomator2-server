package com.testguard.uiautomator2.server;

import android.content.Context;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import com.testguard.uiautomator2.common.exceptions.SessionRemovedException;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

/**
 * 框架主服务
 */
public class ServerInstrumentation {
    private static ServerInstrumentation instance;
    private HttpServer serverThread;
    private PowerManager.WakeLock wakeLock;
    private Context context;
    private int serverPort;
    private  boolean isStopServer;

    private ServerInstrumentation(Context ctx, int port) {
        this.serverPort = port;
        this.context = ctx;
        if (!isValidPort(serverPort)) {
            throw new RuntimeException(("Invalid port: " + serverPort));
        }
    }
    private boolean isValidPort(int port) {
        return port >= 1024 && port <= 65535;
    }

    public boolean isStopServer(){
        return isStopServer;
    }


    public static synchronized ServerInstrumentation getInstance(Context ctx, int serverPort) {
        if (instance == null) {
            instance = new ServerInstrumentation(ctx,serverPort);
        }
        return instance;
    }

    public void startServer() throws InterruptedException, SessionRemovedException {
        if (serverThread != null && serverThread.isAlive()) {
            return;
        }
        if(serverThread == null && isStopServer){
            throw new SessionRemovedException("Delete Session has been invoked");
        }
        if (serverThread != null) {
            Logger.error("Stopping Test Guard Server");
            stopServer();
        }
        serverThread = new HttpServer(this.serverPort);
        serverThread.start();
        Logger.info("Test Guard server startup on " + this.serverPort);
    }

    public void stopServer() {
        try {
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                }catch(Exception e){/* ignore */}
                wakeLock = null;
            }
            stopServerThread();

        } finally {
            instance = null;
        }
    }

    private void stopServerThread()  {
        if (serverThread == null) {
            return;
        }
        if (!serverThread.isAlive()) {
            serverThread = null;
            return;
        }
        Logger.info("stop Test Guard server");
        serverThread.stopLooping();
        serverThread.interrupt();
        try {
            serverThread.join();
        } catch (InterruptedException ignored) {
        }
        serverThread = null;
        isStopServer = true;
    }

    private class HttpServer extends Thread{
        private AndroidServer androidServer;
        private Looper looper;
        public HttpServer(int port){
            androidServer = new AndroidServer(port);
        }

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            startServer();
            Looper.loop();
        }
        public void startServer(){
            // Get a wake lock to stop the cpu going to sleep
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "Test Guard");
            try {
                wakeLock.acquire();
                Device.getInstance().getUiDevice().wakeUp();
//                Config.setDefaultValue();
            } catch (SecurityException e) {
                Logger.error("Security Exception", e);
            } catch (RemoteException e) {
                Logger.error("Remote Exception while waking up", e);
            }
            try {
                androidServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void interrupt() {
            androidServer.stop();
            super.interrupt();
        }

        public void stopLooping() {
            if (looper == null) {
                return;
            }
            looper.quit();
        }
    }
}
