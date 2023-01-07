package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Style;
import org.jsoup.nodes.Element;

public abstract class Component {
    protected Style style = new Style();
    public Component() {
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    public abstract String expr();

    public abstract Element toHTML();

    @Override
    public String toString() {
        return expr();
    }
}