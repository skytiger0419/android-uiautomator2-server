package com.testguard.uiautomator2.handler;

import android.os.Environment;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 指定目录获取文件列表
 * Created by Administrator on 2017/6/13.
 */
public class FileList extends BaseCommandHandler {
    private  List<File> fileList  = new ArrayList<>();
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            Hashtable<String, Object> paramas = command.params();
            String dir = (String) paramas.get("dir");
            int mode = (Integer) paramas.get("mode");
            int showtype = (Integer) paramas.get("showtype");
            String device_dir = null;
            switch (mode) {
                case 1:
                    device_dir = Environment.getExternalStorageDirectory() + "/" + dir;
                    break;
                case 2:
                    device_dir = Device.getInstance().getExtStoragePath(true) + "/" + dir;
                    break;
            }
            if (new File(device_dir).exists()) {
                getFiles(device_dir);
                if (showtype == 0) {
                    return getSuccessResult(getFileName());
                } else {
                    return getSuccessResult(toFileJson());
                }
            } else {
                return getErrorResult("file dir not exists");
            }
        } catch (Exception e) {
            return getErrorResult(e.getMessage());
        }
    }

    private String getFileName(){
        if(fileList.size() ==0){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(File f: fileList){
            sb.append(f.getName() + "\n");
        }
        return sb.toString();
    }


    private String toFileJson() throws JSONException{
        if(fileList.size() ==0){
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for(File f: fileList){
            JSONObject json = new JSONObject();
            json.put("filePath",f.getAbsolutePath());
            json.put("size",f.getTotalSpace());
            jsonArray.put(json.toString());
        }
        return jsonArray.toString();
    }

    private void getFiles(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                fileList.add(files[i]);
            }
        }
    }

//    private void getFiles(String path){
//        File dir = new File(path);
//        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
////                String fileName = files[i].getName();
//                if (files[i].isDirectory()) { // 判断是文件还是文件夹
//                    getFiles(files[i].getAbsolutePath()); // 获取文件绝对路径
//                } else{ // 判断文件名是否以.avi结尾
////                    String strFileName = files[i].getAbsolutePath();
//                    fileList.add(files[i]);
//                }
//            }
//        }
//
//    }
}
