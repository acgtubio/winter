package eydsh.winter;

import eydsh.winter.annotations.Injectable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {
    private final int DIR_SCAN_MAX_DEPTH = 10;

    public PackageScanner() {}

    public List<Class<?>> getInjectables(Class<?> clazz) throws IOException, URISyntaxException {
        String packageName = this.getPackageName(clazz);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String path =  packageName.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();

        List<File> files = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File file = new File(url.toURI());

            scanFilesInPackage(new File[]{file}, files, 0);
        }

        return null;
    }

    private void scanFilesInPackage(File[] currentFiles, List<File> fileAccumulator, int depth) {
        if (depth >  DIR_SCAN_MAX_DEPTH) {
            return;
        }

        for (File file : currentFiles) {
            if (file.isDirectory()) {
                this.scanFilesInPackage(file.listFiles(), fileAccumulator, depth + 1);
            }
            if (file.isFile()) {
                fileAccumulator.add(file);
            }
        }
    }

    private Class<?> convertFileToClass(File file) {
        // TODO: Add implementation
        return null;
    }

    private boolean isInjectable(Class<?> clazz) {
        return clazz.isAnnotationPresent(Injectable.class);
    }

    private String getPackageName(Class<?> clazz) {
        return clazz.getPackageName();
    }
}
