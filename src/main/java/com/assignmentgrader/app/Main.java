package com.assignmentgrader.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    private static final String filePath = "assignment-grader\\src\\main\\resources\\assets";
    private static final Path extractedJavaFiles = Paths.get("assignment-grader\\target\\unzipped");
    private static final Path compiledFilesDirectory = Paths.get("assignment-grader\\target\\classes");
        private static char[] studentID;
        private static String fileName;
        
            public static void main(String[] args) {
                Extractor extractor = new ZipFileExtractor();
                SubmissionExtractor handler = new SubmissionExtractor(extractor);
        
                // extracts zip folders in src/main/resources/assets and puts java files in target/unzipped
                File folder = new File(filePath);
                if(folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles();
                    if( files != null ) {
                        for ( File file : files ) {
                            studentID = file.getName().substring(0,9).toCharArray();
                            fileName = file.getName().replaceAll("\\.zip$", "");
                            try {
                                handler.extractSubmission(file.getAbsolutePath());
                            } catch (IOException e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                        }
                    } 
                }
                
                // compiles the extracted java files
                JavaFileCompiler compiler = new JavaFileCompiler(extractedJavaFiles, compiledFilesDirectory);
                compiler.compileJavaFiles();
        
                // evaluate
                Evaluator chatBot = new ChatBotEvaluator();
                Evaluator chatBotPlatform = new ChatBotPlatformEvaluator();
                Evaluator chatBotGenerator = new ChatBotGeneratorEvaluator();
                
                // load compiled classes and run evaluations
                List<String> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>();
                ClassLoader loader = new ClassLoader();
                try {
                    Class<?> chatBotClass = loader.loadClass("ChatBot");
                    list1 = chatBot.runEvaluation(chatBotClass);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
    
                try {
                    Class<?> chatBotPlatformClass = loader.loadClass("ChatBotPlatform");
                    list2 = chatBotPlatform.runEvaluation(chatBotPlatformClass);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
    
                try {
                    Class<?> chatBotGeneratorClass = loader.loadClass("ChatBotGenerator");
                    list3 = chatBotGenerator.runEvaluation(chatBotGeneratorClass);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
    
                // generate PDF Report
                try {
                    Path outputDirectory = Paths.get(extractedJavaFiles.toString() + "\\" + fileName);
                    List<String> mergedList = new ArrayList<>(list1); mergedList.addAll(list2); mergedList.addAll(list3);
                    List<String> scoreResults = List.of("generateResponse method should return a formatted response and increment response counter.");
                    PDFReportGenerator reportGenerator = new PDFReportGenerator(outputDirectory, studentID, mergedList, scoreResults);
                    reportGenerator.generatePDFReport();
                } catch (IOException e) {
                    System.err.println("Error");
                }

    }

}
