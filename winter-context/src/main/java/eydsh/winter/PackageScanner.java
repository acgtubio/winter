package eydsh.winter;

import eydsh.winter.annotations.Component;
import eydsh.winter.annotations.Injectable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

public class PackageScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackageScanner.class);
    private final int DIR_SCAN_MAX_DEPTH = 10;

    public PackageScanner() {
    }

    public List<Class<?>> getClassesInPackage(Class<?> clazz) throws IOException, URISyntaxException, ClassNotFoundException {
        String packageName = this.getPackageName(clazz);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String path =  packageName.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();


        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            scanClasses(url, classes, packageName, classLoader);
        }

        return classes;
    }

    public List<Class<?>> getInjectables(List<Class<?>> clazzes) {
        return clazzes.stream().filter(this::isInjectable).toList();
    }

    public List<Class<?>> getComponents(List<Class<?>> clazzes) {
        return clazzes.stream().filter(this::isComponent).toList();
    }

    private void scanClasses(URL url, List<Class<?>> classes, String packageName, ClassLoader classLoader) throws URISyntaxException, IOException, ClassNotFoundException {
        String protocol = url.getProtocol();

        if (protocol.equals("file")) {
            scanFiles(url, classes, packageName, classLoader);
        } else if (protocol.equals("jar")) {
            scanJar(url, classes);
        }
    }

    private void scanFiles(URL url, List<Class<?>> clazzes, String packageName, ClassLoader classLoader) throws URISyntaxException, ClassNotFoundException {
        File file = new File(url.toURI());

        List<String> classPathCollection = new ArrayList<>();
        scanInjectableFilesInPackage(new File[]{file}, classPathCollection, packageName, 0);

        for (String classPath : classPathCollection) {
            try {
                Class<?> clazz = Class.forName(classPath, false, classLoader);

                if (!clazz.isAnnotation() &&
                        !clazz.isInterface() &&
                        !clazz.isEnum() &&
                        !clazz.isRecord() &&
                        !clazz.isLocalClass() &&
                        !Modifier.isAbstract(clazz.getModifiers())
                ) {
                    clazzes.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("Cannot find class: {}", e.getMessage());
            }
        }
    }

    private void scanJar(URL url, List<Class<?>> clazzes) throws URISyntaxException, IOException {
        File file = new File(url.toURI());

        scanClassInJar(url);
    }

    private void scanInjectableFilesInPackage(File[] currentFiles, List<String> fileAccumulator, String packageName, int depth) {
        if (depth >  DIR_SCAN_MAX_DEPTH) {
            return;
        }

        for (File file : currentFiles) {
            String newPackage = packageName;
            if (depth > 0) {
                newPackage = packageName + "." + file.getName();
            }

            if (file.isDirectory()) {
                this.scanInjectableFilesInPackage(
                        file.listFiles(),
                        fileAccumulator,
                        newPackage,
                        depth + 1
                );
            }

            if (file.isFile() && file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "");
                fileAccumulator.add(packageName + "." + fileName);
            }
        }
    }

    private void scanClassInJar(URL url) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        JarFile jarFile = conn.getJarFile();

    }

    private Class<?> convertFileToClass(File file) {
        return null;
    }

    private boolean isInjectable(Class<?> clazz) {
        return clazz.isAnnotationPresent(Injectable.class);
    }

    private boolean isComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    private String getPackageName(Class<?> clazz) {
        return clazz.getPackageName();
    }
}
