package com.assignmentgrader.app;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A facade class that simplifies the process of compiling and loading Java classes.
 * Combines {@link JavaFileCompiler} and {@link CustomClassLoader} to handle compilation
 * and dynamic class loading.
 */
public class CompilationFacade {
    private JavaFileCompiler compiler;
    private CustomClassLoader classLoader;
    private boolean success;
    
    /**
     * Constructs a {@code CompilationFacade} with the specified source and output directories.
     *
     * @param sourceDir The directory containing the Java source files to compile.
     * @param outputDir The directory where compiled `.class` files should be stored.
     */
    public CompilationFacade(Path sourceDir, Path outputDir) {
        this.compiler = new JavaFileCompiler(sourceDir, outputDir);
        this.classLoader = new CustomClassLoader(outputDir.toString());
        this.success = false;
    }

    /**
     * Compiles the Java source files and loads a specified class by its name.
     *
     * @param className The fully qualified name of the class to load.
     * @return An {@link Optional} containing the loaded class if successful, or an empty {@link Optional} if compilation or loading fails.
     */
    public Optional<Class<?>> compileAndLoadClass(String className) {
        boolean compilationSuccess = compiler.compileJavaFiles();
        if(!compilationSuccess) {
            System.err.println("Compilation Failed...");
            this.success = false;
        } else {
            this.success = true;
        }
        try {
            Class<?> loadedClass = classLoader.loadClass(className);
            return Optional.of(loadedClass);
        } catch (Exception e) {
            System.err.println("Loading " + className + " failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Indicates whether the last compilation attempt was successful.
     *
     * @return {@code true} if the last compilation was successful; {@code false} otherwise.
     */
    public boolean isSuccessful() {
        return this.success;
    }
}
