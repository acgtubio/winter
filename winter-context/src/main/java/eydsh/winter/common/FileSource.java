package eydsh.winter.common;

public class FileSource implements ScannedClass {
    String packageName;

    FileSource(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }
}
