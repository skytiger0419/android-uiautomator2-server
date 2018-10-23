package com.testguard.uiautomator2.handler;

import android.os.RemoteException;

import com.testguard.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.model.OrientationEnum;
import com.testguard.uiautomator2.model.internal.CustomUiDevice;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;
import static com.testguard.uiautomator2.utils.Device.getInstance;

/**
 * 获取屏幕方向或设置屏幕方向
 * params:{} 有值则为设置，无值则为获取,未实现坐标旋转
 *
 * */
public class Orientation extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        final Hashtable<String, Object> params = command.params();
        if (params.containsKey("orientation")) {
            // Set the rotation
            final String orientation = (String) params.get("orientation");
            try {
                return handleRotation(orientation);
            } catch (final Exception e) {
                return getErrorResult("Unable to rotate screen: " + e.getMessage());
            }
        } else {
            // Get the rotation
            return getRotation();
        }

    }

    /**
     * Returns the current rotation
     *
     * @return {@link AndroidCommandResult}
     */
    private AndroidCommandResult getRotation() {
        String res = null;
        int rotation = getInstance().getUiDevice().getDisplayRotation();
        final OrientationEnum currentRotation = OrientationEnum.fromInteger(rotation);
        Logger.debug("Current rotation: " + currentRotation);
        switch (currentRotation) {
            case ROTATION_0:
            case ROTATION_180:
                res = "PORTRAIT";
                break;
            case ROTATION_90:
            case ROTATION_270:
                res = "LANDSCAPE";
                break;
        }
        if (res != null) {
            return getSuccessResult(res);
        } else {
            return getErrorResult("Get orientation did not complete successfully");
        }
    }

    /**
     * Set the desired rotation
     *
     * @param orientation The rotation desired (LANDSCAPE or PORTRAIT)
     * @return {@link AndroidCommandResult}
     * @throws RemoteException
     * @throws InterruptedException
     */
    private AndroidCommandResult handleRotation(final String orientation)
            throws RemoteException, InterruptedException {
        int rotation = getInstance().getUiDevice().getDisplayRotation();
        OrientationEnum desired = null;
        OrientationEnum current = OrientationEnum.fromInteger(rotation);
        Logger.debug("Desired orientation: " + orientation);
        Logger.debug("Current rotation: " + current);
        if (orientation.equalsIgnoreCase("LANDSCAPE")) {
            switch (current) {
                case ROTATION_0:
                    getInstance().getUiDevice().setOrientationRight();
                    desired = OrientationEnum.ROTATION_270;
                    break;
                case ROTATION_180:
                    getInstance().getUiDevice().setOrientationLeft();
                    desired = OrientationEnum.ROTATION_270;
                    break;
                default:
                    return getSuccessResult("Already in landscape mode.");
            }
        } else if (orientation.equalsIgnoreCase("PORTRAIT")) {
            switch (current) {
                case ROTATION_90:
                case ROTATION_270:
                    getInstance().getUiDevice().setOrientationNatural();
                    desired = OrientationEnum.ROTATION_0;
                    break;
                default:
                    return getSuccessResult("Already in portrait mode.");
            }
        } else {
            if (orientation.equalsIgnoreCase("LEFT")) {
                switch (current) {
                    case ROTATION_0:
                        getInstance().getUiDevice().setOrientationLeft();
                        desired = OrientationEnum.ROTATION_90;
                        break;
                    case ROTATION_90:
                        getInstance().getUiDevice().setOrientationLeft();
                        desired = OrientationEnum.ROTATION_180;
                        break;
                    case ROTATION_180:
                        getInstance().getUiDevice().setOrientationLeft();
                        desired = OrientationEnum.ROTATION_270;
                        break;
                    case ROTATION_270:
                        getInstance().getUiDevice().setOrientationLeft();
                        desired = OrientationEnum.ROTATION_0;
                        break;
                }

            } else if (orientation.equalsIgnoreCase("RIGHT")) {
                switch (current) {
                    case ROTATION_0:
                        getInstance().getUiDevice().setOrientationRight();
                        desired = OrientationEnum.ROTATION_270;
                        break;
                    case ROTATION_90:
                        getInstance().getUiDevice().setOrientationRight();
                        desired = OrientationEnum.ROTATION_0;
                        break;
                    case ROTATION_180:
                        getInstance().getUiDevice().setOrientationRight();
                        desired = OrientationEnum.ROTATION_90;
                        break;
                    case ROTATION_270:
                        getInstance().getUiDevice().setOrientationRight();
                        desired = OrientationEnum.ROTATION_180;
                        break;
                }
            } else if (orientation.equalsIgnoreCase("NATURAL")) {
                switch (current) {
                    case ROTATION_90:
                    case ROTATION_180:
                    case ROTATION_270:
                        getInstance().getUiDevice().setOrientationNatural();
                        desired = OrientationEnum.ROTATION_0;
                        break;
                    default:
                        return getSuccessResult("Already in Natural mode.");
                }
            }
        }
       return verifyRotation(desired);
    }

    /**
     * 设置方向，以x ,y,z驱动
     * @param x
     * @param y
     * @param z
     * @return
     * @throws InvalidCoordinatesException
     * @throws RemoteException
     * @throws InterruptedException
     */
    private AndroidCommandResult handleRotation(int x, int y, int z) throws InvalidCoordinatesException, RemoteException, InterruptedException{
        if ( x!=0 || y!=0 || !( z==0 || z==90 || z==180 || z==270 )) {
            throw new InvalidCoordinatesException("Unable to Rotate Device. Invalid rotation, valid params x=0, y=0, z=(0 or 90 or 180 or 270)");
        }
        OrientationEnum current = OrientationEnum.fromInteger(
                getInstance().getUiDevice().getDisplayRotation());
        OrientationEnum desired = OrientationEnum.fromInteger(z/90);
        if(current == desired) {
            return getSuccessResult(String.format("Already in %s mode", current.getOrientation()));
        }
        switch(desired) {
            case ROTATION_0:
            case ROTATION_90:
            case ROTATION_180:
            case ROTATION_270:
                CustomUiDevice.getInstance().getInstrumentation().getUiAutomation().setRotation(desired.getValue());
                break;
        }

        return verifyRotation(desired);
    }

    /**
     * 验证方向是否与期望一致
     * @param desired
     * @return
     * @throws InterruptedException
     */
    public AndroidCommandResult verifyRotation(OrientationEnum desired) throws InterruptedException {
        OrientationEnum current = OrientationEnum.fromInteger(getInstance().getUiDevice().getDisplayRotation());
        // If the orientation has not changed,
        // busy wait until the TIMEOUT has expired
        final int TIMEOUT = 2000;
        final long then = System.currentTimeMillis();
        long now = then;
        while (current != desired && now - then < TIMEOUT) {
            Thread.sleep(100);
            now = System.currentTimeMillis();
            current = OrientationEnum.fromInteger(getInstance().getUiDevice().getDisplayRotation());
        }
        if (current != desired) {
            return getErrorResult("Set the orientation, but app refused to rotate.");
        }
        return getSuccessResult("Rotation (" + current.getOrientation() + ") successful.");
    }

}
