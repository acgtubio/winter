package eydsh.winter.testcases;

import eydsh.winter.annotations.Component;
import eydsh.winter.annotations.Inject;

@Component
public class TestComponent1 {
    private NoInjectClass1 noInjectClass1;

    @Inject
    public TestComponent1(NoInjectClass1 noInjectClass1) {
        this.noInjectClass1 = noInjectClass1;
    }
}
