package com.assignmentgrader.app;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JavaFileCompiler {

    private final Path sourceDir;
    private final Path outputDir;

    public JavaFileCompiler(Path sourceDir, Path outputDir) {
        this.sourceDir = sourceDir;
        this.outputDir = outputDir;
    }

    /**
     * Compiles all Java files in the source directory and outputs .class files to the output directory.
     */
    public void compileJavaFiles() {
        // Ensure the output directory exists
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.err.println("Could not create output directory: " + e.getMessage());
            return;
        }

        // Get the system Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("No Java compiler available. Ensure you are running this with a JDK, not a JRE.");
            return;
        }

        // Use the compiler's file manager
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            // Set the output location for .class files
            fileManager.setLocation(javax.tools.StandardLocation.CLASS_OUTPUT, Arrays.asList(outputDir.toFile()));

            // Find all .java files in the source directory
            List<File> javaFiles = findJavaFiles(sourceDir);

            if (javaFiles.isEmpty()) {
                System.out.println("No Java files found to compile.");
                return;
            }

            // Compile the Java files
            Iterable<? extends javax.tools.JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
            boolean success = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

            if (success) {
                System.out.println("Compilation successful. .class files are located in " + outputDir.toAbsolutePath());
            } else {
                System.out.println("Compilation failed. Check source files for errors.");
            }
        } catch (IOException e) {
            System.err.println("Error during compilation: " + e.getMessage());
        }
    }

    /**
     * Finds all .java files in the given directory.
     *
     * @param directory the directory to search for .java files
     * @return a list of .java files in the directory
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

    public static void main(String[] args) {
        Path sourceDir = Paths.get("assignment-grader\\target\\unzipped"); // Directory where the Java files are extracted
        Path outputDir = Paths.get("assignment-grader\\target\\classes");  // Directory to place compiled .class files

        JavaFileCompiler compiler = new JavaFileCompiler(sourceDir, outputDir);
        compiler.compileJavaFiles();
    }
}
