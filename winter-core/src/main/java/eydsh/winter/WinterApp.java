package eydsh.winter;

import eydsh.winter.annotations.Winter;

public class WinterApp {
    private final PackageScanner scanner;

    public WinterApp() {
        this.scanner = new PackageScanner();
    }

    public void run(Class<?> mainClass) {
        if (!mainClass.isAnnotationPresent(Winter.class)) {
            return;
        }
    }
}
