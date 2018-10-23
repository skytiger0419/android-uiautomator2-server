package com.testguard.uiautomator2.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiObjectNotFoundException;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 获取元素截图或屏幕截图
 * 
 */
public class Screenshot extends BaseCommandHandler{
	private File screenshot = new File(InstrumentationRegistry.getTargetContext().getFilesDir(), "screenshot.png");

	@Override
	public AndroidCommandResult execute(final AndroidCommand command) {
		try {
			final Hashtable<String, Object> params = command.params();
			if (screenshot.exists()) {
				screenshot.delete();
			}
			if(params.containsKey("scale") && params.containsKey("quality")){
				double scale_double = (Double) params.get("scale");
				float scale = (float)scale_double;
				int quality = (Integer)params.get("quality");
				Device.getInstance().getUiDevice().takeScreenshot(screenshot, scale, quality);
			}
			if (command.isElementCommand()) {
				try {
					final AndroidElement el = command.getElement();
					if (!screenshot.exists()) {
						return getSuccessResult(false);
					}
					elementTakeSceenshot(el, screenshot);
				} catch (UiObjectNotFoundException e) {
					return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT,
							e.getMessage());
				} catch (Exception e) {
					return getErrorResult("Unknown error");
				}
			}
			return getSuccessResult(screenshot.getPath());
		} catch (JSONException e) {
			return getErrorResult("Error take screen shot");
		}

	}

	/**
	 * 获取元素截图
	 * 
	 * @param el
	 * @param picture
	 * @throws UiObjectNotFoundException
	 * @throws IOException
	 */
	public void elementTakeSceenshot(AndroidElement el, File picture)
			throws UiObjectNotFoundException, IOException {
		FileOutputStream out = null;
		try{
			Bitmap bitmap = BitmapFactory
					.decodeStream(new FileInputStream(picture));
			final Rect bounds = el.getBounds();
			Bitmap dest = Bitmap.createBitmap(bitmap, bounds.left, bounds.top,
					bounds.width(), bounds.height());
			out = new FileOutputStream(picture);
			dest.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
		}finally{
			out.close();
		}
		

	}
}