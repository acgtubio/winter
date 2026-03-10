package eydsh.winter.testcases;

import eydsh.winter.annotations.Inject;

public class TestComponent2 {

    private TestClass1 testClass1;
    private TestClass2 testClass2;
    private TestClass3 testClass3;

    @Inject
    public TestComponent2(TestClass1 testClass1, TestClass2 testClass2, TestClass3 testClass3) {
        this.testClass1 = testClass1;
        this.testClass2 = testClass2;
        this.testClass3 = testClass3;
    }
}
