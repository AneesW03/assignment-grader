package com.assignmentgrader.app;
import java.io.File;
import java.io.IOException;

public interface Extractor {
    void extract(String filePath, File destination) throws IOException;
}
