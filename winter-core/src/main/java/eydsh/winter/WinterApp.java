package eydsh.winter;

import eydsh.winter.annotations.Winter;
import eydsh.winter.common.WinterRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class WinterApp {
    private final DependencyRegistry dependencyRegistry;
    private static final Logger LOGGER = LoggerFactory.getLogger(WinterApp.class);

    public WinterApp() {
        this.dependencyRegistry = new DependencyRegistry();
    }

    public void run(Class<?> mainClass) {
        LOGGER.info("Welcome to Winter.");
        if (!mainClass.isAnnotationPresent(Winter.class)) {
            return;
        }

        LOGGER.info("Registering dependencies.");
        try {
            this.dependencyRegistry.startDependencyRegistry(mainClass);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        Optional<WinterRunner> optionalRunner = this.dependencyRegistry.getWinterRunner();
        if (optionalRunner.isEmpty()) {
            LOGGER.warn("No runner.");
            return;
        }

        LOGGER.info("Starting runner.");
        WinterRunner runner = optionalRunner.get();

        runner.run();
    }
}
