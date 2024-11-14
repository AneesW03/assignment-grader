package com.assignmentgrader.app;
import java.nio.file.Path;
import java.util.Optional;

public class CompilationFacade {
    private final JavaFileCompiler compiler;
    private final CustomClassLoader classLoader;

    public CompilationFacade(Path sourceDir, Path outputDir) {
        this.compiler = new JavaFileCompiler(sourceDir, outputDir);
        this.classLoader = new CustomClassLoader(outputDir.toString());
    }

    public Optional<Class<?>> compileAndLoadClass(String className) {
        boolean compilationSuccess = compiler.compileJavaFiles();
        if(!compilationSuccess) {
            System.err.println("Compilation Failed...");
        }
        try {
            Class<?> loadedClass = classLoader.loadClass(className);
            return Optional.of(loadedClass);
        } catch (Exception e) {
            System.err.println("Loading " + className + " failed: " + e.getMessage());
            return Optional.empty();
        }
    }
}
