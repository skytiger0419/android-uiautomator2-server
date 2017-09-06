package com.yep.uiautomator2.executorserver;

import com.yep.uiautomator2.handler.BaseCommandHandler;
import org.json.JSONException;
import org.json.JSONObject;
import com.yep.uiautomator2.handler.*;
import com.yep.uiautomator2.utils.Logger;
import java.util.HashMap;

/**
 * Command execution dispatch class. This class relays commands to the various
 * handlers.
 */
public class AndroidCommandExecutor {

    private static HashMap<String, BaseCommandHandler> map = new HashMap<String, BaseCommandHandler>();

    static {
        //设备操作命令
        map.put("ping", new Ping());
        map.put("clear", new Clear());
        map.put("swipe", new Swipe());
        map.put("swipeOrientation", new SwipeOrientation());
        map.put("scrollTo", new ScrollTo());
        map.put("scrollOrientation", new ScrollOrientation());
        map.put("rotation", new Rotation());
        map.put("flick", new Flick());
        map.put("drag", new Drag());
        map.put("pinch", new Pinch());
        map.put("click", new Click());
        map.put("touchLongClick", new TouchLongClick());
        map.put("touchDown", new TouchDown());
        map.put("touchUp", new TouchUp());
        map.put("touchMove", new TouchMove());
        map.put("performMultiPointerGesture", new MutiPointerGesture());
        map.put("pressBack", new PressBack());
        map.put("pressKeyCode", new PressKeyCode());
        map.put("longPressKeyCode", new LongPressKeyCode());
        map.put("takeScreenshot", new Screenshot());
        //元素属性值查找
        map.put("getText", new GetText());
        map.put("setText", new SetText());
        map.put("setTextNew", new SetTextNew());
        map.put("getAttribute", new GetAttribute());
        map.put("getSize", new GetSize());
        map.put("getLocation", new GetLocation());
        map.put("find", new Find());
        map.put("getParent", new GetParent());
        map.put("getDeviceSize", new GetDeviceSize());
        map.put("wake", new Wakeup());
        map.put("lock", new Lock());
        map.put("orientation", new Orientation());
        map.put("waitForIdle", new WaitForIdle());
        map.put("openNotification", new OpenNotification());
        map.put("openQuickSetting", new OpenQuickSetting());
        map.put("openRecentApp", new OpenRecentApps());
        map.put("compressedLayoutHierarchy", new CompressedLayoutHierarchy());
        map.put("source", new Source());
        map.put("rawSource", new RawSource());
        map.put("getDataDir", new GetDataDir());
        map.put("updateString", new AppString());
        //自定义辅助操作
        map.put("clearCache", new ClearCache());
//        map.put("monitor", new Monitors());
        map.put("toast", new Toast());
        map.put("startApp", new StartApp());
        map.put("wifi", new Wifi());
        map.put("bluetooth", new Bluetooth());
        map.put("calllog", new CallLog());
        map.put("contact", new Contact());
        map.put("gps", new Gps());
        map.put("settingAction", new StartSettingAction());
        map.put("fileList", new FileList());
        map.put("light", new Light());
        map.put("audio", new Audio());
        map.put("camaraLight", new CamaraLight());
        map.put("data", new Data());
        map.put("configurator", new ConfiguratorHandler());
        map.put("deviceinfo", new DeviceInfoHandler());
        map.put("devicepath", new DevicePath());
        map.put("sim_info", new Sim());
        map.put("uiWatcher", new UiWatcherHandler());
        map.put("clearUiCache", new ClearUiCache());
        map.put("timezone", new TimeZoneHandler());
        map.put("jumpApp", new JumpAppDetail());
    }

    /**
     * Gets the handler out of the map, and executes the command.
     *
     * @param jsonData
     * @return
     */
    public AndroidCommandResult execute(final JSONObject jsonData) {
        AndroidCommand command = new AndroidCommand(jsonData);
        try {
            Logger.debug("Got command action: " + command.action());
            try {
                Logger.debug(command.action() + (command.params() == null ? "" : command.params().toString()));
            } catch (Exception e) {
                Logger.debug(command.action());
            }
            if (map.containsKey(command.action())) {
                return map.get(command.action()).execute(command);
            } else {
                return new AndroidCommandResult(WDStatus.UNKNOWN_COMMAND,
                        "Current not support " + command.action());
            }
        } catch (final JSONException e) {
            return new AndroidCommandResult(WDStatus.JSON_DECODER_ERROR,
                    "Could not decode action/params of command, please check format!");
        }catch (final Exception e){
            e.printStackTrace();
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        }
    }
}
