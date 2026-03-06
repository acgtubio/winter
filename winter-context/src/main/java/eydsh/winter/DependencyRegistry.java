package eydsh.winter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyRegistry {
    private ConcurrentHashMap<String, Object> singletonDependencies;

    public DependencyRegistry() {
        this.singletonDependencies = new ConcurrentHashMap<>();
    }

    public void registerInjectables(List<Class<?>> injectables) {

    }
}
