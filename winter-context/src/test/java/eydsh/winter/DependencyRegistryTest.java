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
        try {
            registry.mapDependencies(injectables);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void acyclicTestWithComponent() {
        DependencyRegistry registry = new DependencyRegistry();

        Set<Class<?>> injectables = Set.of(TestClass1.class, TestClass2.class, TestClass3.class);
        Set<Class<?>> components = Set.of(TestComponent2.class);

        registry.setInjectables(injectables);
        registry.setComponents(components);

        try {
            registry.mapDependencies(injectables);
            registry.mapDependencies(components);
            System.out.println(registry.showGraph());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testStartRegistry() {
        DependencyRegistry registry = new DependencyRegistry();

        try {
            registry.startDependencyRegistry(TestComponent2.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
