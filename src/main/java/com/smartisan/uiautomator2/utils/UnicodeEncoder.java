package com.smartisan.uiautomator2.utils;
import java.nio.charset.Charset;


public class UnicodeEncoder {
    private static final Charset M_UTF7 = Charset.forName("x-IMAP-mailbox-name");
    private static final Charset ASCII = Charset.forName("US-ASCII");


    public static String encode(final String text) {
        byte[] encoded = text.getBytes(M_UTF7);
        String ret = new String(encoded, ASCII);
        if (ret.charAt(ret.length()-1) != text.charAt(text.length()-1) && !ret.endsWith("-")) {
            // in some cases there is a problem and the closing tag is not added
            // to the encoded text (for instance, with `ü`)
            //
            // but first, sometimes it is just that the original string is too long
            // and things get confused
            if (text.length() >= 2) {
                Logger.debug("Encoding error. Splitting text and trying again.");
                int middle = text.length() / 2;
                ret = encode(text.substring(0, middle)) + encode(text.substring(middle));
            } else {
                Logger.debug("Closing tag missing. Adding.");
                ret = ret + "-";
            }
        }
        return ret;
    }

    public static boolean needsEncoding(final String text) {
        // TODO: yet to implement

       /* char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int cp = Character.codePointAt(chars, i);
            if (cp > 0x7F || cp == '&') {
                // Selenium uses a Unicode PUA to cover certain special characters
                // see https://code.google.com/p/selenium/source/browse/java/client/src/org/openqa/selenium/Keys.java
                // these should juse be passed through as is.
                return !(cp >= 0xE000 && cp <= 0xE040);
            }
        }*/
        return false;
    }
}
