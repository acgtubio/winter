package com.eydsh.winter;

import java.util.concurrent.ConcurrentHashMap;

public class DependencyRegistry {
    private ConcurrentHashMap<String, Object> singletonDependencies;

    public DependencyRegistry() {
        this.singletonDependencies = new ConcurrentHashMap<>();
    }

    public void registerInjectables() {

    }
}
