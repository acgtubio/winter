package eydsh.winter.testcases;

import eydsh.winter.annotations.Inject;
import eydsh.winter.annotations.Injectable;

@Injectable
public class TestClass1 {
    private TestClass2 testClass2;

    @Inject
    public TestClass1 (TestClass2 testClass2) {
        this.testClass2 = testClass2;
    }
}
