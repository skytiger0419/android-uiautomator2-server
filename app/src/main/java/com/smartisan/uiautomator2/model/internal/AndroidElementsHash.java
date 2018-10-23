package com.smartisan.uiautomator2.model.internal;

import com.smartisan.uiautomator2.model.AndroidElement;

import java.util.Hashtable;
import java.util.regex.Pattern;

/**
 * A cache of elements that the app has seen.
 */
public class AndroidElementsHash {

    private static final Pattern endsWithInstancePattern = Pattern.compile(".*INSTANCE=\\d+]$");
    private static AndroidElementsHash instance;
    private final Hashtable<String, AndroidElement> elements;
    private Integer counter;

    /**
     * Constructor
     */
    public AndroidElementsHash() {
        counter = 0;
        elements = new Hashtable<String, AndroidElement>();
    }

    public static AndroidElementsHash getInstance() {
        if (AndroidElementsHash.instance == null) {
            AndroidElementsHash.instance = new AndroidElementsHash();
        }
        return AndroidElementsHash.instance;
    }

    /**
     * Return an element given an Id.
     *
     * @return {@link AndroidElement}
     */
    public AndroidElement getElement(final String key) {
        return elements.get(key);
    }
}
