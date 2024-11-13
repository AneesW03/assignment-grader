package com.assignmentgrader.app;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {
    public Class<?> loadClass(String className) throws Exception {
        File file = new File("assignment-grader\\target\\classes");
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
    
        try (URLClassLoader classLoader = new URLClassLoader(urls)) {
            return classLoader.loadClass(className);
        }
    }
}
