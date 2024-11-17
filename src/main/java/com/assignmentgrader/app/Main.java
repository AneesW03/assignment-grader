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

    public static void main(String[] args) {
        ExtractorContext extractor = new ExtractorContext();
        extractor.setExtractionStrategy(new ZipExtractorStrategy());

        // extract all zip files in src/main/resources/assets.
        File folder = new File(filePath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if(files != null) {
                for (File file : files) {
                    try {
                        extractor.extractSubmission(file.getAbsolutePath());
                    } catch (Exception e) {
                        System.err.println("Error here: " + e.getMessage());
                    }
                }
            }
        }

        File outDirectory = new File(compiledFilesDirectory.toString());
            if (outDirectory.exists() && outDirectory.isDirectory()) {
                File[] classFiles = outDirectory.listFiles();
                if (classFiles != null) {
                    for (File classFile : classFiles) {
                        if(classFile.getName().endsWith(".class")) {
                            classFile.delete();
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

                        Path javaFiles = Paths.get(file.getAbsolutePath());
                        Path simulationFilePath = Paths.get("target\\unzipped\\" + file.getName() + "\\ChatBotSimulation.java");

                        CompilationFacade facade = new CompilationFacade(javaFiles, compiledFilesDirectory);
                        EvaluationResult result = new EvaluationResult();
                        String testResults = "";
                        int score = 0;
                        Boolean compilationSuccess = true;
                        Boolean testSuccess = true;

                        try {
                            Optional<Class<?>> chatBotClass = facade.compileAndLoadClass("ChatBot");
                            if(facade.isSuccessful()) {

                                if(chatBotClass.isPresent()) {
                                    Class<?> chatBot = chatBotClass.get();
                                    Evaluator chatBotEvaluator = new ChatBotEvaluator(chatBot);
                                    result = chatBotEvaluator.evaluate();
                                    if(!chatBotEvaluator.isPassed()) {
                                        testSuccess = false;
                                    }
                                    testResults += result.toPDFString();
                                    score += result.getTotalScore();
                                } else {
                                    System.out.println("ChatBot class does not exist...");

                                }
                            } else {
                                compilationSuccess = false;
                            }
                        } catch (Exception e) {
                            System.err.println("ChatBot: Error - " + e.getMessage());
                        }
                
                        try {
                            Optional<Class<?>> chatBotPlatformClass = facade.compileAndLoadClass("ChatBotPlatform");
                            if(facade.isSuccessful()) {
                                if(chatBotPlatformClass.isPresent()) {
                                    Class<?> chatBotPlatform = chatBotPlatformClass.get();
                                    Evaluator chatBotPlatformEvaluator = new ChatBotPlatformEvaluator(chatBotPlatform);
                                    result = chatBotPlatformEvaluator.evaluate();
                                    if(!chatBotPlatformEvaluator.isPassed()) {
                                        testSuccess = false;
                                    }
                                    testResults += result.toPDFString();
                                    score += result.getTotalScore();
                                } else {
                                    System.out.println("ChatBotPlatform class does not exist...");
                                }
                            } else {
                                compilationSuccess = false;
                            }
                        } catch (Exception e) {
                            System.err.println("ChatBotPlatform: Error - " + e.getMessage());
                        }
                
                        try {
                            Optional<Class<?>> chatBotGeneratorClass = facade.compileAndLoadClass("ChatBotGenerator");
                            if(facade.isSuccessful()) {
                                if(chatBotGeneratorClass.isPresent()) {
                                    Class<?> chatBotGenerator = chatBotGeneratorClass.get();
                                    Evaluator chatBotGeneratorEvaluator = new ChatBotGeneratorEvaluator(chatBotGenerator);
                                    result = chatBotGeneratorEvaluator.evaluate();
                                    if(!chatBotGeneratorEvaluator.isPassed()) {
                                        testSuccess = false;
                                    }
                                    testResults += result.toPDFString();
                                    score += result.getTotalScore();
                                } else {
                                    System.out.println("ChatBotGenerator class does not exist...");
                                }
                            } else {
                                compilationSuccess = false;
                            }
                        } catch (Exception e) {
                            System.err.println("ChatBotGenerator: Error - " + e.getMessage());
                        }
                        
                        try {
                            Optional<Class<?>> chatBotSimulationClass = facade.compileAndLoadClass("ChatBotSimulation");
                            if(facade.isSuccessful()) {
                                if(chatBotSimulationClass.isPresent()) {
                                    Class<?> chatBotSimulation = chatBotSimulationClass.get();
                                    Evaluator chatBotSimulEvaluator = new ChatBotSimulationEvaluator(chatBotSimulation, simulationFilePath);
                                    result = chatBotSimulEvaluator.evaluate();
                                    if(!chatBotSimulEvaluator.isPassed()) {
                                        testSuccess = false;
                                    }
                                    testResults += result.toPDFString();
                                    score += result.getTotalScore();
                                } else {
                                    System.out.println("ChatBotSimulation class does not exist...");
                                }
                            } else {
                                compilationSuccess = false;
                            }
                        } catch (Exception e) {
                            System.err.println("ChatBotSimulation: Error - " + e.getMessage());
                        }

                        EvaluationResult general = new EvaluationResult();
                        general.setClassName("General");
                        if (compilationSuccess) {
                            general.addTestResults(15, "Compiles and runs without error.");
                        } else {
                            general.addTestResults(0, "Does not compile or run.");
                        }

                        if (testSuccess) {
                            general.addTestResults(10, "All tests for all classes passed.");
                        } else {
                            general.addTestResults(0, "Not all tests passed successfully.");
                        }
                        
                        testResults += general.toPDFString();
                        score += general.getTotalScore();
                
                        try {
                            String totalScore = Integer.toString(score);
                            Path outputDirectory = javaFiles;
                            PDFReportGenerator reportGenerator = new PDFReportGenerator(outputDirectory, studentID, testResults, totalScore);
                            reportGenerator.generatePDFReport();
                        } catch (IOException e) {
                            System.err.println("Error generating PDF: " + e.getMessage());
                        }

                    }
                }
            }
        }
    }     
}