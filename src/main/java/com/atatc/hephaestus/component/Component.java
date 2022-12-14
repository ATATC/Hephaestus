package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Style;
import com.atatc.hephaestus.function.Consumer;
import com.vladsch.flexmark.util.ast.Node;

public abstract class Component implements HTMLCapable, MDCapable {
    protected Style style = new Style();

    public Component() {
    }

    public ComponentConfig getConfig() {
        return getClass().getAnnotation(ComponentConfig.class);
    }

    public String getTagName() {
        ComponentConfig componentConfig = getConfig();
        if (componentConfig == null) return "undefined";
        return componentConfig.tagName();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    protected void forEach(Consumer<? super Component> action, int depth) {
        action.accept(this, depth);
    }

    public void forEach(Consumer<? super Component> action) {
        forEach(action, 0);
    }

    public abstract String expr();

    // fixme: remove
    @Override
    public Node toMarkdown() {
        return null;
    }

    @Override
    public String toString() {
        return expr();
    }
}
