package eydsh.winter.testcases;

import eydsh.winter.annotations.Inject;
import eydsh.winter.annotations.Injectable;

@Injectable
public class TestClass2 {
    private TestClass3 testClass3;

    @Inject
    public TestClass2(TestClass3 testClass3) {
        this.testClass3 = testClass3;
    }
}
