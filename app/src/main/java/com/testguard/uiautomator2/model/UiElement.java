package com.testguard.uiautomator2.model;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.testguard.uiautomator2.utils.Attribute;

import java.util.List;
import java.util.Map;

/**
 * UiElement that implements the common operations.
 *
 * @param <R> the type of the raw element this class wraps, for example, View or
 *        AccessibilityNodeInfo
 * @param <E> the type of the concrete subclass of UiElement
 */
public abstract class UiElement<R, E extends UiElement<R, E>> {
  // These two attribute names are used for debugging only.
  // The two constants are used internally and must match to-uiautomator.xsl.
  public static final String ATTRIB_VISIBLE_BOUNDS = "VisibleBounds";
  public static final String ATTRIB_NOT_VISIBLE = "NotVisible";
  public AccessibilityNodeInfo node;


  @SuppressWarnings("unchecked")
  public <T> T get(Attribute attribute) {
    return (T) getAttributes().get(attribute);
  }

  public String getText() {
    return get(Attribute.TEXT);
  }

  public String getContentDescription() {
    return get(Attribute.CONTENT_DESC);
  }

  public String getClassName() {
    return get(Attribute.CLASS);
  }

  public String getResourceId() {
    return get(Attribute.RESOURCE_ID);
  }

  public String getPackageName() {
    return get(Attribute.PACKAGE);
  }

  public boolean isCheckable() {
    return (Boolean) get(Attribute.CHECKABLE);
  }

  public boolean isChecked() {
    return (Boolean) get(Attribute.CHECKED);
  }

  public boolean isClickable() {
    return (Boolean) get(Attribute.CLICKABLE);
  }

  public boolean isEnabled() {
    return (Boolean) get(Attribute.ENABLED);
  }

  public boolean isFocusable() {
    return (Boolean) get(Attribute.FOCUSABLE);
  }

  public boolean isFocused() {
    return (Boolean) get(Attribute.FOCUSED);
  }

  public boolean isScrollable() {
    return (Boolean) get(Attribute.SCROLLABLE);
  }

  public boolean isLongClickable() {
    return (Boolean) get(Attribute.LONG_CLICKABLE);
  }

  public boolean isPassword() {
    return (Boolean) get(Attribute.PASSWORD);
  }

  public boolean isSelected() {
    return (Boolean) get(Attribute.SELECTED);
  }

  public int getIndex() { return  (Integer)get(Attribute.INDEX); }

  protected abstract List<E> getChildren();

  public Rect getBounds() {
    return get(Attribute.BOUNDS);
  }

  public int getSelectionStart() {
    Integer value = get(Attribute.SELECTION_START);
    return value == null ? 0 : value;
  }

  public int getSelectionEnd() {
    Integer value = get(Attribute.SELECTION_END);
    return value == null ? 0 : value;
  }

  public boolean hasSelection() {
    final int selectionStart = getSelectionStart();
    final int selectionEnd = getSelectionEnd();

    return selectionStart >= 0 && selectionStart != selectionEnd;
  }

  protected abstract Map<Attribute, Object> getAttributes();
}
