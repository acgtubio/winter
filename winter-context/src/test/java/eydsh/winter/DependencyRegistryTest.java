package eydsh.winter;

import eydsh.winter.common.CircularDependencyException;
import eydsh.winter.testcases.*;
import org.junit.Test;

import java.util.Set;

public class DependencyRegistryTest {
    @Test(expected = CircularDependencyException.class)
    public void cyclicTest() {
        DependencyRegistry registry = new DependencyRegistry();

        Set<Class<?>> injectables = Set.of(TestClass1.class, TestClass2.class, TestClass3.class);

        registry.setInjectables(injectables);
        registry.mapDependencies(injectables);
    }

    @Test
    public void acyclicTestWithComponent() {
        DependencyRegistry registry = new DependencyRegistry();

        Set<Class<?>> injectables = Set.of(TestClass1.class, TestClass2.class, TestClass3.class);
        Set<Class<?>> components = Set.of(TestComponent2.class);

        registry.setInjectables(injectables);
        registry.setComponents(components);

        registry.mapDependencies(injectables);
        registry.mapDependencies(components);

        System.out.println(registry.showGraph());
    }

}
