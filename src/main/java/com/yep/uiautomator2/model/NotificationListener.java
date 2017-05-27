package com.yep.uiautomator2.model;

import android.app.UiAutomation;
import android.view.accessibility.AccessibilityEvent;
import com.yep.uiautomator2.core.UiAutomatorBridge;
import com.yep.uiautomator2.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.currentTimeMillis;

/**
 * 监听toast信息
 */
public final class NotificationListener {
    private static List<CharSequence> toastMessages = new ArrayList<CharSequence>();
    private final static NotificationListener INSTANCE = new NotificationListener();
    private final Listener listener = new Listener();
    private boolean stopLooping = false;
    private final int TOAST_CLEAR_TIMEOUT = 3500;

    private NotificationListener(){

    }

    public static NotificationListener getInstance(){
        return INSTANCE;
    }

    /**
     * Listens for Notification Messages
     */
    public void start(){
        if(!listener.isAlive()){
            listener.setDaemon(true);
            listener.start();
            stopLooping = false;
        }

    }

    public void stop(){
        stopLooping = true;
    }

    public List<CharSequence> getToastMSGs() {
        return toastMessages;
    }

    private class Listener extends Thread{

        private long previousTime = currentTimeMillis();

        public void run() {
            while (!stopLooping) {
                AccessibilityEvent accessibilityEvent = null;
                toastMessages = init();
                //return true if the AccessibilityEvent type is NOTIFICATION type
                UiAutomation.AccessibilityEventFilter eventFilter = new UiAutomation.AccessibilityEventFilter() {
                    @Override
                    public boolean accept(AccessibilityEvent event) {
                        return event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
                    }
                };
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Not performing any event.
                    }
                };

                try {
                    //wait for AccessibilityEvent filter
                    accessibilityEvent = UiAutomatorBridge.getInstance().getUiAutomation()
                            .executeAndWaitForEvent(runnable /*executable event*/, eventFilter /* event to filter*/, 500 /*time out in ms*/);
                } catch (Exception ignore) {}

                if (accessibilityEvent != null) {
                    toastMessages = accessibilityEvent.getText();
                    previousTime = currentTimeMillis();
                }
            }
        }

        public List<CharSequence> init(){
            if( currentTimeMillis() - previousTime  > TOAST_CLEAR_TIMEOUT) {
                return new ArrayList<CharSequence>();
            }
            return toastMessages;
        }
    }
}
