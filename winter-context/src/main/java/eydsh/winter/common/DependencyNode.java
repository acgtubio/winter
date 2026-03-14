package eydsh.winter.common;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DependencyNode {
    private final List<DependencyNode> parents;
    private final String classPath;
    private final Class<?> type;
    private final List<DependencyNode> dependencies;
    private Constructor<?> constructor;

    public DependencyNode(Class<?> clazz) {
        this.classPath = clazz.getName();
        this.parents = new ArrayList<>();
        this.type = clazz;
        this.dependencies = new ArrayList<>();
        try {
            this.constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            this.constructor = null;
        }
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

    public Class<?> getType() {
        return this.type;
    }

    public List<DependencyNode> getDependencies() {
        return this.dependencies;
    }

    public void addDependency(DependencyNode node) {
        if (this.dependencies.contains(node)) {
            return;
        }
        this.dependencies.add(node);
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    @Override
    public String toString() {
        return this.classPath;
    }
}
