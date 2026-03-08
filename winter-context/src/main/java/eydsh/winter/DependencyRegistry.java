package eydsh.winter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyRegistry.class);
    private final PackageScanner packageScanner;
    private ConcurrentHashMap<String, Object> singletonDependencies;

    public DependencyRegistry() {
        this.singletonDependencies = new ConcurrentHashMap<>();
        this.packageScanner = new PackageScanner();
    }

    public void startDependencyRegistry(Class<?> clazz) {
        try {
            List<Class<?>> classes = this.packageScanner.getClassesInPackage(clazz);
            List<Class<?>> injectables = this.packageScanner.getInjectables(classes);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void registerInjectables(List<Class<?>> injectables) {

    }
}
