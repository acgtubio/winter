package eydsh.winter.common;

import eydsh.winter.PackageScanner;
import org.junit.Test;

public class DependencyGraphTest {
    @Test(expected = CircularDependencyException.class)
    public void testCyclicGraph() {
        DependencyGraph dependencyGraph = new DependencyGraph();

        dependencyGraph.addDependency(PackageScanner.class, FileSource.class);
        dependencyGraph.addDependency(FileSource.class, DependencyNode.class);
        dependencyGraph.addDependency(DependencyNode.class, PackageScanner.class);
    }

    @Test
    public void testAcyclicGraph() {
        DependencyGraph dependencyGraph = new DependencyGraph();

        dependencyGraph.addDependency(PackageScanner.class, FileSource.class);
        dependencyGraph.addDependency(FileSource.class, DependencyNode.class);
    }
}
