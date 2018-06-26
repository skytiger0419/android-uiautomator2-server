package com.smartisan.uiautomator2.manage;

import android.content.Context;
import android.media.AudioManager;
import com.smartisan.uiautomator2.utils.Device;

/**
 * 调整音量模式
 * Created by Administrator on 2017/6/20.
 */
public class MAudioManage {

    private static Context context = Device.getInstance().getContext();

    public static void setRingMode(int mode){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mode);
    }

    public static int getRingMode(){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    public static boolean getHandSetStatus(){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.isWiredHeadsetOn();
    }


}
