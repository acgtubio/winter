package eydsh.winter.common;

import java.util.ArrayList;
import java.util.List;

public class DependencyNode {
    private final List<DependencyNode> parents;
    private final String classPath;

    public DependencyNode(Class<?> clazz) {
        this.classPath = clazz.getName();
        this.parents = new ArrayList<>();
    }

    public String getClassPath() {
        return this.classPath;
    }

    public void addParent(DependencyNode parentNode) {
        if (this.parents.contains(parentNode)) {
            return;
        }
        this.parents.add(parentNode);
    }

    public List<DependencyNode> getParents() {
        return this.parents;
    }

    @Override
    public String toString() {
        return this.classPath;
    }
}
