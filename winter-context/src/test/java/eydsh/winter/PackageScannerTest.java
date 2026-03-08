package eydsh.winter;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PackageScannerTest {

    @Test
    public void testPackageScanner() {
        class SomeClass {
            public List<Class<?>> run() throws IOException, URISyntaxException, ClassNotFoundException {
                PackageScanner packageScanner = new PackageScanner();

                return packageScanner.getClassesInPackage(SomeClass.class);
            }
        }

        try {
            List<Class<?>> result = new SomeClass().run();

            // TODO: Add test here lmao.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            System.out.println("womp womp");
        }
    }
}
