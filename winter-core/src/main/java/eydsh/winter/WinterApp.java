package eydsh.winter;

import eydsh.winter.annotations.Winter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WinterApp {
    private final PackageScanner scanner;
    private final DependencyRegistry dependencyRegistry;
    private static final Logger LOGGER = LoggerFactory.getLogger(WinterApp.class);

    public WinterApp() {
        this.scanner = new PackageScanner();
        this.dependencyRegistry = new DependencyRegistry();
    }

    public void run(Class<?> mainClass) {
        if (!mainClass.isAnnotationPresent(Winter.class)) {
            return;
        }

        try {
            this.dependencyRegistry.startDependencyRegistry(mainClass);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }
}
