package com.atatc.hephaestus.config;

import com.atatc.hephaestus.component.*;
import com.atatc.hephaestus.exception.MissingFieldException;
import com.atatc.hephaestus.parser.Parser;
import org.reflections.Reflections;

import java.util.*;

public final class Config {
    private final static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private final Map<String, Parser<?>> parserMap = new HashMap<>();

    private Config() {
        scanPackages("com.atatc.hephaestus.component", "com.atatc.hephaestus.skeleton");
    }

    public void scanPackage(String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ComponentConfig.class);
        for (Class<?> clz : classes) {
            try {
                if (!Component.class.isAssignableFrom(clz)) continue;
                ComponentConfig componentConfig = clz.getAnnotation(ComponentConfig.class);
                Parser<?> parser = (Parser<?>) clz.getField("PARSER").get(null);
                putParser(componentConfig.tagName(), parser);
            } catch (NoSuchFieldException ignored) {
                throw new MissingFieldException(clz, "PARSER");
            } catch (IllegalAccessException ignored) {}
        }
    }

    public void scanPackages(String... packages) {
        for (String pkg : packages) scanPackage(pkg);
    }

    public void putParser(String componentName, Parser<?> parser) {
        parserMap.put(componentName, parser);
    }

    public Parser<?> getParser(String componentName) {
        return parserMap.get(componentName);
    }
}
