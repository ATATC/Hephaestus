package com.atatc.hephaestus;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.MultiComponent;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.component.UnsupportedComponent;
import com.atatc.hephaestus.config.Config;
import com.atatc.hephaestus.exception.BadFormat;
import com.atatc.hephaestus.exception.ComponentNotClosed;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.skeleton.Skeleton;
import com.atatc.hephaestus.skeleton.Body;
import org.jetbrains.annotations.NotNull;

public final class Hephaestus {
    public static Component parseExpr(@NotNull String expr) throws BadFormat {
        if (expr.isEmpty()) return null;
        if (Text.wrappedBy(expr, '[', ']')) return MultiComponent.PARSER.parse(expr.substring(1, expr.length() - 1));
        UnsupportedComponent temp = new UnsupportedComponent();
        temp.expr = expr;
        int i = Text.indexOf(expr, ':');
        if (i < 0) return Text.PARSER.parse(expr.substring(1, expr.length() - 1));
        temp.tagName = expr.substring(1, i).replaceAll(" ", "");
        temp.inner = expr.substring(i + 1, expr.length() - 1);
        if (Text.wrappedBy(expr, '<', '>')) {
            Skeleton skeleton = Skeleton.PARSER.parse(temp.inner);
            skeleton.setName(temp.tagName);
            return skeleton;
        }
        if (!Text.wrappedBy(expr, '{', '}')) throw new ComponentNotClosed(expr);
        Parser<?> parser = Config.getInstance().getParser(temp.tagName);
        if (parser == null) return temp;
        return parser.parse(temp.inner);
    }

    public static Body parse(@NotNull String expr) throws BadFormat {
        if (expr.isEmpty()) return null;
        Skeleton skeleton = null;
        if (Text.startsWith(expr, '<')) {
            int endIndex = Text.lastIndexOf(expr, '>') + 1;
            if (endIndex >= 0) {
                skeleton = Skeleton.PARSER.parse(expr.substring(0, endIndex));
                expr = expr.substring(endIndex);
            }
        }
        return new Body(skeleton, parseExpr(expr));
    }
}
