package com.smartisan.uiautomator2.manage;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import java.util.List;

/**
 * 控制摄像头，闪光灯
 * Created by Administrator on 2017/6/20.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CamaraLightManage {
    private static CamaraLightManage instance;
    private static Context context;
    /**
     * Camera相机硬件操作类
     */
    private Camera mCamera = null;
    /**
     * Camera2相机硬件操作类
     */
    private CameraManager mCameraManager = null;
    private boolean isOpen = false;

    private CamaraLightManage() {
        context = Device.getInstance().getContext();
    }

    public static CamaraLightManage getInstance() {
        if (instance == null) {
            instance = new CamaraLightManage();
        }
        return instance;
    }

    public void init() {
        // 初始化Camera硬件
        if (isLOLLIPOP()) {
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        } else {
            mCamera = Camera.open();
        }
    }

    /**
     * 调用Camera2开启闪光灯
     * @throws CameraAccessException
     */
    private void openCamera2Flash() throws CameraAccessException {
        mCameraManager.setTorchMode("0",true);
    }

    private void closeCamera2Flash() throws CameraAccessException{
        mCameraManager.setTorchMode("0",false);
    }


    /**
     * 通过设置Camera打开闪光灯
     * @param camera
     */
    public void turnLightOnCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // 开启闪光灯
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }
        }
    }

    /**
     * 通过设置Camera关闭闪光灯
     * @param camera
     */
    public void turnLightOffCamera(Camera camera) {
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // 关闭闪光灯
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                mCamera.release();
            }
        }
    }

    /**
     * 判断Android系统版本是否 >= LOLLIPOP(API21)
     *
     * @return boolean
     */
    private boolean isLOLLIPOP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    public void open() {
        if(!isOpen){
            if (isLOLLIPOP()) {
                try {
                    openCamera2Flash();
                } catch (CameraAccessException e) {
                    Logger.error(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                turnLightOnCamera(mCamera);
            }
        }
        isOpen = true;
    }

    public void close() {
        if(isOpen){
            if (isLOLLIPOP()) {
                try{
                    closeCamera2Flash();
                } catch (CameraAccessException e) {
                    Logger.error(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                turnLightOffCamera(mCamera);
            }
        }
        isOpen = false;
    }
}
