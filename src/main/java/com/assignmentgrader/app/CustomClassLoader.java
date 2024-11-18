package com.assignmentgrader.app;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader {
    private String classPath;

    public CustomClassLoader(String classPath) {
        this.classPath = classPath;
    }

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