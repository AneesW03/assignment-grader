package com.assignmentgrader.app;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
* A custom class loader for dynamically loading compiled `.class` files.
* Useful for loading classes generated at runtime into the JVM.
*/
public class CustomClassLoader {
    private String classPath;

    /**
     * Constructs a {@code CustomClassLoader} with the specified class path.
     *
     * @param classPath The directory containing the compiled `.class` files.
     */
    public CustomClassLoader(String classPath) {
        this.classPath = classPath;
    }
    
    /**
     * Dynamically loads a class by its name from the specified class path.
     *
     * @param className The fully qualified name of the class to load.
     * @return The {@code Class} object representing the loaded class.
     * @throws Exception If the class cannot be loaded.
     */
    public Class<?> loadClass(String className) throws Exception {
        File file = new File(classPath);
        
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        
        try (URLClassLoader classLoader = new URLClassLoader(urls)) {
            Class<?> clazz = classLoader.loadClass(className);
            return clazz;
        }
    }

}
