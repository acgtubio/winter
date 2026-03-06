package com.eydsh.winter;

import java.util.List;
import java.util.Optional;

public class PackageScanner {
    public PackageScanner() {}

    public List<Class<?>> getClassesInPackage(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        return null;
    }

    public String getPackageName(Class<?> clazz) {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String packageName;

        Optional<StackWalker.StackFrame> callerFrame = walker.walk(frame ->
                frame
                    .filter( f -> f.getClassName().equals(clazz.getName()))
                    .findFirst()
        );

        if (callerFrame.isEmpty()) {
            return "";
        }
        StackWalker.StackFrame frame = callerFrame.get();
        System.out.println(clazz.getPackageName());

        return frame.getMethodName();
    }
}
