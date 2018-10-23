package com.testguard.uiautomator2.common.monitor;

import android.support.test.uiautomator.UiWatcher;
import com.testguard.uiautomator2.utils.Logger;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义uiwathcers方案，用时间任务处理
 * Created by Administrator on 2017/8/10.
 */
public class UiWatchers2 {
    private static UiWatchers2 instance = null;
    private final Timer timer = new Timer("WatchTimer");
    private final HashMap<String, UiWatcher> mWatchers = new HashMap<String, UiWatcher>();
    private boolean mInWatcherContext = false;
    private UiWatchers2(){

    }
    public static UiWatchers2 getInstance(){
        if(instance == null){
            synchronized (UiWatchers2.class){
                instance = new UiWatchers2();
            }
        }
        return instance;
    }

    /**
     * 启动监控
     */
    public void start(){
        final TimerTask updateWatchers = new TimerTask() {
            @Override
            public void run() {
                try {
                    runWatchers();
                } catch (final Exception e) {
                }
            }
        };
        timer.scheduleAtFixedRate(updateWatchers, 100, 200);
    }

    /**
     * 添加watcher
     * @param name
     * @param uiWatcher
     */
    public void add(String name, UiWatcher uiWatcher){
        mWatchers.put(name, uiWatcher);
    }

    /**
     * 移除指定触发
     * @param name
     */
    public void remove(String name){
        if(mWatchers.containsKey(name)){
            mWatchers.remove(name);
        }
    }

    /**
     * 清理所有触发
     */
    public void clearAll(){
        mWatchers.clear();
    }
    /**
     * This method forces all registered watchers to run.
     */
    private void runWatchers(){
        if(mInWatcherContext){
            return;
        }
        for (String watcherName : mWatchers.keySet()) {
            UiWatcher watcher = mWatchers.get(watcherName);
            if (watcher != null) {
                try {
                    mInWatcherContext = true;
                    watcher.checkForCondition();
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        }
        mInWatcherContext = false;
    }
}
