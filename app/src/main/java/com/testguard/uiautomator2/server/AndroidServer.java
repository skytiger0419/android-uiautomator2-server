package com.testguard.uiautomator2.server;

import android.support.test.uiautomator.Configurator;
import com.testguard.uiautomator2.common.monitor.UiWatchers2;
import com.testguard.uiautomator2.executorserver.AndroidCommandExecutor;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

/**
 * httpserver
 * Created by Administrator on 2017/4/18.
 */
public class AndroidServer extends NanoHTTPD{
    private final AndroidCommandExecutor mAndroidExecServer = new AndroidCommandExecutor();
    private static String STOP = "/stop"; //停止服务
    private UiWatchers2 uiWatchers2 = UiWatchers2.getInstance();
    public AndroidServer(int port) {
        super(port);
        Configurator.getInstance().setWaitForIdleTimeout(500);
        uiWatchers2.start();
    }

    @Override
    public Response serve(IHTTPSession session) {
        if(STOP.equalsIgnoreCase(session.getUri())){
            this.stop();
            return returnSuccess("test guard exit");
        }
        if (!Method.POST.equals(session.getMethod())) {
            return returnFailure("only use post method");
        }
        String errMsg = null;
        try {
            Map<String, String> files = new HashMap<String, String>(); //接收前端参数
            session.parseBody(files);
            JSONObject json = null;
            for(String tmp :files.values()){
                json = new JSONObject(tmp);
            }
            if(json != null){
                Logger.debug(json.toString());
                AndroidCommandResult result = mAndroidExecServer.execute(json);
                if(json.getString("action").equalsIgnoreCase("takeScreenshot")){
                    return formatFile(result.getValue());
                }
                return formatJson(result.toString());
            }else{
                errMsg = "empty params";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errMsg = e.getMessage();
        } catch ( IOException e) {
            errMsg = e.getMessage();
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
            errMsg = e.getMessage();
        }
        return returnFailure(errMsg);
    }

    public Response returnSuccess(String data){
        return  formatJson(String.format("{\"status\":0,\"msg\":%s}",data));
    }
    public Response returnFailure(String data){
        return formatJson(String.format("{\"status\":1,\"msg\":%s}",data));
    }
    public Response formatJson(String data){
        return newFixedLengthResponse(Response.Status.OK, "application/json", data);
    }

    public Response formatFile(String path) throws FileNotFoundException {
        return newChunkedResponse(Response.Status.OK, "image/png", new FileInputStream(path));
    }
}
