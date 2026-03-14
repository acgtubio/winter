package eydsh.winter.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DependencyGraph {
    private final List<DependencyNode> graph;

    public DependencyGraph() {
        this.graph = new ArrayList<>();
    }

    public void addDependency(Class<?> dependency, Class<?> parent) throws CircularDependencyException, NoSuchMethodException {
        this.addSingleDependency(dependency, parent);
    }

    /**
     * This will register the node in the dependency graph if it is not being tracked already.
     *
     * @param clazz The injectable to be tracked.
     */
    public void registerNode(Class<?> clazz) throws NoSuchMethodException {
        Optional<DependencyNode> optionalParent = this.getNode(clazz.getName());

        DependencyNode parentNode;
        if (optionalParent.isPresent()) {
            return;
        }

        parentNode = new DependencyNode(clazz);
        graph.add(parentNode);
    }

    public Optional<DependencyNode> getNode(String classPath) {
        return graph
                .stream()
                .filter(node ->
                        node.getClassPath().equals(classPath)
                )
                .findFirst();
    }

    public void addDependency(Iterable<Class<?>> dependency, Class<?> parent) throws CircularDependencyException, NoSuchMethodException {
        for (Class<?> clazz: dependency) {
            this.addSingleDependency(clazz, parent);
        }
    }

    private void addSingleDependency(Class<?> dependency, Class<?> parent) throws CircularDependencyException, NoSuchMethodException {
        Optional<DependencyNode> optionalParent = graph
                .stream()
                .filter(node ->
                        node.getClassPath().equals(parent.getName())
                )
                .findFirst();

        Optional<DependencyNode> optionalChild = graph
                .stream()
                .filter(node ->
                        node.getClassPath().equals(dependency.getName())
                )
                .findFirst();

        DependencyNode parentNode;
        if (optionalParent.isEmpty()) {
            parentNode = new DependencyNode(parent);
            graph.add(parentNode);
        } else {
            parentNode = optionalParent.get();
        }

        DependencyNode childDependency;
        if (optionalChild.isEmpty()) {
            childDependency = new DependencyNode(dependency);
            graph.add(childDependency);
        } else {
            childDependency = optionalChild.get();
        }

        childDependency.addParent(parentNode);
        parentNode.addDependency(childDependency);

        boolean isAcyclic = checkAcyclic(childDependency, childDependency);
        if (!isAcyclic) {
            throw new CircularDependencyException("Circular dependency.");
        }
    }

    // Change the algorithm. Very inefficient.
    protected boolean checkAcyclic(DependencyNode currentNode, DependencyNode target) {
        for (DependencyNode node :  currentNode.getParents()) {
            if (node == null) {
                continue;
            }

            if (node == target) {
                return false;
            }

            return checkAcyclic(node, target);
        }

        return true;
    }

    public List<DependencyNode> getGraph() {
        return this.graph;
    }

    @Override
    public String toString() {
        StringBuilder template = new StringBuilder();
        for (DependencyNode node : this.graph) {
            template.append(node.getClassPath());
            template.append(": ");
            template.append("Parents: ");
            template.append(node.getParents());
            template.append(" Dependencies: ");
            template.append(node.getDependencies());
            template.append(" Constructor: ");
            template.append(node.getConstructor());
            template.append("\n");
        }

        return template.toString();
    }
}
