package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.ReflectionUtils;
import com.testguard.uiautomator2.utils.XMLHierarchy;
import org.json.JSONException;
import org.w3c.dom.Document;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 获取屏幕节点数据
 * Created by Administrator on 2017/3/12.
 */

public class Source extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            ReflectionUtils.clearAccessibilityCache();
            final Document doc = (Document) XMLHierarchy.getFormattedXMLDoc();
            final TransformerFactory tf = TransformerFactory.newInstance();
            final StringWriter writer = new StringWriter();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String xmlString = writer.getBuffer().toString();
            return getSuccessResult(xmlString);

        } catch (final TransformerConfigurationException e) {
            return getErrorResult("Something went terribly wrong while converting xml document to string:" + e.toString());
        } catch (final TransformerException e) {
            return getErrorResult( "Could not parse xml hierarchy to string: " + e.toString());
        } catch (UiAutomator2Exception e) {
            return getErrorResult(e.toString());
        }
    }
}
