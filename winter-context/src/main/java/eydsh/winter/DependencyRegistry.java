package eydsh.winter;

import eydsh.winter.annotations.Inject;
import eydsh.winter.common.CircularDependencyException;
import eydsh.winter.common.DependencyGraph;
import eydsh.winter.common.DependencyNode;
import eydsh.winter.common.UncontrolledDependencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DependencyRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyRegistry.class);
    private final PackageScanner packageScanner;
    private final ConcurrentHashMap<String, Object> singletonDependencies;
    private Set<Class<?>> injectables;
    private Set<Class<?>> components;
    private final DependencyGraph dependencyGraph;

    public DependencyRegistry() {
        this.singletonDependencies = new ConcurrentHashMap<>();
        this.packageScanner = new PackageScanner();
        this.dependencyGraph = new DependencyGraph();
    }

    protected void startDependencyRegistry(Class<?> clazz) {
        try {
            Set<Class<?>> classes = this.packageScanner.getClassesInPackage(clazz);
            this.injectables = this.packageScanner.getInjectables(classes);
            this.components = this.packageScanner.getComponents(classes);

            this.mapDependencies(injectables);
            this.mapDependencies(components);

            this.initializeDependencies(this.dependencyGraph);

            System.out.println(this.singletonDependencies);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    protected void initializeDependencies(DependencyGraph graph) {
        for (DependencyNode node : graph.getGraph()) {
            initializeClass(node);
        }
    }

    private boolean isInitialized(DependencyNode node) {
        return this.singletonDependencies.contains(node.getClassPath());
    }

    protected void initializeClass(DependencyNode node) {
        if (isInitialized(node)) {
            return;
        }

        List<DependencyNode> dependencyNodes = node.getDependencies();
        for (DependencyNode dependencyNode : dependencyNodes) {
            initializeClass(dependencyNode);
        }

        List<Object> dependencies = new ArrayList<>();
        dependencyNodes.forEach(n -> {
            dependencies.add(this.singletonDependencies.get(n.getClassPath()));
        });

        Constructor<?> constructor = node.getConstructor();

        try {
            Object obj = constructor.newInstance(dependencies.toArray());

            this.singletonDependencies.put(node.getClassPath(), obj);
        } catch (Exception e) {
            LOGGER.error("Error instantiating {}: {}", node.getClassPath(), e.getMessage());
        }
    }

    protected void mapDependencies(Set<Class<?>> injectables) throws NoSuchMethodException {
         for (Class<?> clazz : injectables) {
             this.dependencyGraph.registerNode(clazz);

             Constructor<?>[] constructors = clazz.getConstructors();
             Constructor<?> nominatedConstructor = this.getNominatedConstructor(constructors);

             // If no nominated constructor, that means that the constructor is a normal class without injectables.
             if (nominatedConstructor == null) {
                 continue;
             }

             Set<Class<?>> constructorInjectables = this.scanConstructorDependencies(nominatedConstructor);
             if (constructorInjectables == null || constructorInjectables.isEmpty()) {
                 continue;
             }

             // If this has a nominated constructor, use this is as the constructor for the class.
             Optional<DependencyNode> optionalNode = this.dependencyGraph.getNode(clazz.getName());
             optionalNode.ifPresent(node -> node.setConstructor(nominatedConstructor));

             this.dependencyGraph.addDependency(constructorInjectables, clazz);

             mapDependencies(constructorInjectables);
         }
    }

    /**
     * getNominatedConstructor will get the first constructor that contains the @Inject annotation with the valid parameters.
     * It will only be a valid @Inject constructor if its parameters are also controlled/tracked by Winter.
     *
     * @param constructors array of constructors from a class.
     * @return the first valid constructor with @Inject annotation.
     */
    protected Constructor<?> getNominatedConstructor(Constructor<?>[] constructors) {
        // This will check only the first constructor with @Inject.
        for (Constructor<?> constructor : constructors) {
            if (!constructor.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?>[] parameters = constructor.getParameterTypes();

            boolean isValid = validateConstructorParameters(parameters);
            if (!isValid) {
                continue;
            }

            return constructor;
        }

        return null;
    }

    protected Set<Class<?>> scanConstructorDependencies(Constructor<?> constructor) throws UncontrolledDependencyException {
        Class<?>[] parameters = constructor.getParameterTypes();

        boolean isValid = validateConstructorParameters(parameters);
        if (!isValid) {
            throw new UncontrolledDependencyException("Parameters are not controlled by Winter.");
        }

        return Set.of(parameters);
    }

    /**
     *
     * @param parameters List of all the types of parameters
     * @return Returns whether all the parameters are controlled by Winter.
     */
    protected boolean validateConstructorParameters(Class<?>[] parameters) {
        List<Class<?>> filteredParameters = Arrays.stream(parameters).filter(p -> this.injectables.contains(p)).toList();

        return filteredParameters.size() == parameters.length;
    }

    protected void setInjectables(Set<Class<?>> injectables) {
        this.injectables = injectables;
    }

    protected void setComponents(Set<Class<?>> components) {
        this.components = components;
    }

    protected String showGraph() {
        return this.dependencyGraph.toString();
    }

    protected String showRegistry() {
        return this.singletonDependencies.toString();
    }
}
