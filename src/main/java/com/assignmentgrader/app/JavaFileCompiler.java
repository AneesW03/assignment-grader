package com.assignmentgrader.app;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for compiling Java source files.
 * Uses the Java Compiler API to compile `.java` files from a specified source directory
 * and outputs the compiled `.class` files to a specified output directory.
 */
public class JavaFileCompiler {
    private Path sourceDir;
    private Path outputDir;

    /**
     * Constructs a {@code JavaFileCompiler} with the specified source and output directories.
     *
     * @param sourceDir The directory containing the Java source files to compile.
     * @param outputDir The directory where compiled `.class` files should be stored.
     */
    public JavaFileCompiler(Path sourceDir, Path outputDir) {
        this.sourceDir = sourceDir;
        this.outputDir = outputDir;
    }

    /**
     * Compiles all Java source files in the source directory.
     *
     * @return {@code true} if compilation succeeds; {@code false} otherwise.
     */
    public boolean compileJavaFiles() {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.err.println("Could not create output directory: " + e.getMessage());
            return false;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("No Java compiler available. Ensure you are running this with a JDK, not a JRE.");
            return false;
        }

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            fileManager.setLocation(javax.tools.StandardLocation.CLASS_OUTPUT, Arrays.asList(outputDir.toFile()));
            List<File> javaFiles = findJavaFiles(sourceDir);
            if (javaFiles.isEmpty()) {
                System.out.println("No Java files found to compile.");
                return false;
            }

            Iterable<? extends javax.tools.JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
            boolean success = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

            if (success) {
                System.out.println("Compilation successful. .class files are located in " + outputDir.toAbsolutePath());
                return true;
            } else {
                System.out.println("Compilation failed. Check source files for errors.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error during compilation: " + e.getMessage());
            return false;
        }
    }

     /**
     * Finds all `.java` files in the given directory and its subdirectories.
     *
     * @param directory The root directory to search for Java source files.
     * @return A list of Java source files found in the directory.
     */
    private List<File> findJavaFiles(Path directory) {
        try {
            return Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .toList();
        } catch (IOException e) {
            System.err.println("Error finding Java files: " + e.getMessage());
            return List.of();
        }
    }
}
