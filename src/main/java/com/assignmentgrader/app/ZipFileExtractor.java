package com.assignmentgrader.app;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileExtractor implements Extractor {
    @Override
    public void extract (String filePath, File destination) throws IOException {

        // check if destination exists and create it otherwise
        if (!destination.exists()) {
            destination.mkdirs();
        }

        try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                File outputFile = new File(destination, entry.getName());

                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                }
                else {
                    outputFile.getParentFile().mkdirs();

                    if(entry.getName().endsWith(".zip")) {
                        try (FileOutputStream outputStream = new FileOutputStream(outputFile);
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                            
                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = inputStream.read(buffer)) != -1) {
                                bufferedOutputStream.write(buffer, 0, read);
                            }
                        }

                        File nestedDirectory = new File(destination, outputFile.getName().replace(".zip", ""));
                        extract(outputFile.getAbsolutePath(), nestedDirectory);
                        outputFile.delete();
                    }

                    else if(entry.getName().endsWith(".java")) {

                        try (FileOutputStream outputStream = new FileOutputStream(outputFile);
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                            
                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = inputStream.read(buffer)) != -1) {
                                bufferedOutputStream.write(buffer, 0, read);
                            }
                        }
                    }
                }
                inputStream.closeEntry();
            }
        }
    }
}
