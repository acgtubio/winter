package eydsh.winter;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public class PackageScannerTest {

    @Test
    public void testPackageScanner() {
        class SomeClass {
            public Set<Class<?>> run() throws IOException, URISyntaxException, ClassNotFoundException {
                PackageScanner packageScanner = new PackageScanner();

                return packageScanner.getClassesInPackage(SomeClass.class);
            }
        }

        try {
            Set<Class<?>> result = new SomeClass().run();

            // TODO: Add test here lmao.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            System.out.println("womp womp");
        }
    }
}
