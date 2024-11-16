package com.assignmentgrader.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {
    private static final String filePath = "src/main/resources/assets";
    private static final Path extractedJavaFiles = Paths.get("target/unzipped");
    private static final Path compiledFilesDirectory = Paths.get("./target/classes");
    private static char[] studentID;
    private static String fileName;
        
    public static void main(String[] args) {
        ExtractorContext extractor = new ExtractorContext();
        extractor.setExtractionStrategy(new ZipExtractorStrategy());

        File folder = new File(filePath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if( files != null ) {
                for ( File file : files ) {
                    //studentID = file.getName().substring(0,9).toCharArray();
                    //fileName = file.getName().replaceAll("\\.zip$", "");
                    try {
                        extractor.extractSubmission(file.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            } 
        }
        
        File extractedFolder = new File(extractedJavaFiles.toString());
        if(extractedFolder.exists() && extractedFolder.isDirectory()) {
            File[] files = extractedFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        studentID = file.getName().substring(0,9).toCharArray();

                        Path javaFiles = Paths.get(file.getPath());
                        Path classFiles = Paths.get("./target/classes");

                        CompilationFacade facade = new CompilationFacade(javaFiles, classFiles);
                        EvaluationResult result = new EvaluationResult();
                        String testResults = "";
                        int score = 0;
                        Path simulationFilePath = Paths.get("target\\unzipped\\" + file.getName() + "\\ChatBotSimulation.java");

                        try {
                            Optional<Class<?>> chatBotClass = facade.compileAndLoadClass("ChatBot");
                            if(chatBotClass.isPresent()) {
                                Class<?> chatBot = chatBotClass.get();
                                Evaluator chatBotEvaluator = new ChatBotEvaluator(chatBot);
                                result = chatBotEvaluator.evaluate();
                                testResults += result.toPDFString();
                                score += result.getTotalScore();
                            } else {
                                System.out.println("Class does not exist...");
                            }
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                
                        try {
                            Optional<Class<?>> chatBotPlatformClass = facade.compileAndLoadClass("ChatBotPlatform");
                            if(chatBotPlatformClass.isPresent()) {
                                Class<?> chatBotPlatform = chatBotPlatformClass.get();
                                Evaluator chatBotPlatformEvaluator = new ChatBotPlatformEvaluator(chatBotPlatform);
                                result = chatBotPlatformEvaluator.evaluate();
                                testResults += result.toPDFString();
                                score += result.getTotalScore();
                            } else {
                                System.out.println("Class does not exist...");
                            }
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                
                        try {
                            Optional<Class<?>> chatBotGeneratorClass = facade.compileAndLoadClass("ChatBotGenerator");
                            if(chatBotGeneratorClass.isPresent()) {
                                Class<?> chatBotGenerator = chatBotGeneratorClass.get();
                                Evaluator chatBotGeneratorEvaluator = new ChatBotGeneratorEvaluator(chatBotGenerator);
                                result = chatBotGeneratorEvaluator.evaluate();
                                testResults += result.toPDFString();
                                score += result.getTotalScore();
                            } else {
                                System.out.println("Class does not exist...");
                            }
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        
                        try {
                            Optional<Class<?>> chatBotSimulationClass = facade.compileAndLoadClass("ChatBotSimulation");
                            if(chatBotSimulationClass.isPresent()) {
                                Class<?> chatBotSimulation = chatBotSimulationClass.get();
                                Evaluator chatBotSimulEvaluator = new ChatBotSimulationEvaluator(chatBotSimulation, simulationFilePath);
                                result = chatBotSimulEvaluator.evaluate();
                                testResults += result.toPDFString();
                                score += result.getTotalScore();
                            } else {
                                System.out.println("Class does not exist...");
                            }
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                
                        try {
                            String totalScore = Integer.toString(score);
                            Path outputDirectory = javaFiles;
                            PDFReportGenerator reportGenerator = new PDFReportGenerator(outputDirectory, studentID, testResults, totalScore);
                            reportGenerator.generatePDFReport();
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }     
}