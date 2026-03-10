package eydsh.winter;

import eydsh.winter.annotations.Inject;
import eydsh.winter.common.CircularDependencyException;
import eydsh.winter.common.DependencyGraph;
import eydsh.winter.common.UncontrolledDependencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DependencyRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyRegistry.class);
    private final PackageScanner packageScanner;
    private ConcurrentHashMap<String, Object> singletonDependencies;
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

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    protected void initializeDependencies(DependencyGraph graph) {

    }

    protected void mapDependencies(Set<Class<?>> injectables) {
         for (Class<?> clazz : injectables) {
             this.dependencyGraph.registerNode(clazz);

             Constructor<?>[] constructors = clazz.getConstructors();
             Set<Class<?>> constructorInjectables = scanConstructorDependencies(constructors);

             if (constructorInjectables == null || constructorInjectables.isEmpty()) {
                 continue;
             }

             dependencyGraph.addDependency(constructorInjectables, clazz);

             mapDependencies(constructorInjectables);
         }
    }

    protected Set<Class<?>> scanConstructorDependencies(Constructor<?>[] constructors) throws UncontrolledDependencyException {
        // This will check only the first constructor with @Inject.
        for (Constructor<?> constructor : constructors) {
            if (!constructor.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?>[] parameters = constructor.getParameterTypes();

            boolean isValid = validateConstructorParameters(parameters);
            if (!isValid) {
                throw new UncontrolledDependencyException("Parameters are not controlled by Winter.");
            }

            return Set.of(parameters);
        }

        return null;
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
}
