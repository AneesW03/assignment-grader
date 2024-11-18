package com.assignmentgrader.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {
    private static String filePath = "src/main/resources/assets";
    private static Path extractedJavaFiles = Paths.get("target/unzipped");
    private static Path compiledFilesDirectory = Paths.get("target/classes");
    private static char[] studentID;

    public static void main(String[] args) {
        boolean testSuccess = true;
        boolean compilationSuccess = true;

        ExtractorContext extractor = new ExtractorContext();
        extractor.setExtractionStrategy(new ZipExtractorStrategy());

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

        File[] submissions = getSubmissions(extractedJavaFiles);
        for(File submission : submissions) {
            studentID = submission.getName().substring(0,9).toCharArray();
            Path javaFiles = Paths.get("target/unzipped/" + submission.getName());
            Path simulationFilePath = Paths.get("target/unzipped/" + submission.getName() + "/ChatBotSimulation.java");
            String testResults = "";
            int score = 0;

            CompilationFacade facade = new CompilationFacade(javaFiles, compiledFilesDirectory);
            EvaluationResult result = new EvaluationResult();

            Evaluator chatBotEvaluator = new ChatBotEvaluator();
            result = runEvaluation("ChatBot", chatBotEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 36) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            Evaluator chatBotPlatformEvaluator = new ChatBotPlatformEvaluator();
            result = runEvaluation("ChatBotPlatform", chatBotPlatformEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 20) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            Evaluator chatBotGeneratorEvaluator = new ChatBotGeneratorEvaluator();
            result = runEvaluation("ChatBotGenerator", chatBotGeneratorEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 7) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            Evaluator chatBotSimulationEvaluator = new ChatBotSimulationEvaluator(simulationFilePath);
            result = runEvaluation("ChatBotSimulation", chatBotSimulationEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 12) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            EvaluationResult general = new EvaluationResult();
            general.setClassName("General");
            if (testSuccess) { general.addTestResults(10, "All tests for all classes passed."); } 
            else { general.addTestResults(0, "Not all tests passed successfully."); }
                            
            if (compilationSuccess) { general.addTestResults(15, "Compiles and runs successfully."); }
            else { general.addTestResults(0, "Does not compile and run."); }
                                                    
            testResults += general.toPDFString();
            score += general.getTotalScore();
                            
            try {
                String totalScore = Integer.toString(score);
                PDFReportGenerator reportGenerator = new PDFReportGenerator(javaFiles, studentID, testResults, totalScore);
                reportGenerator.generatePDFReport();
            } catch (IOException e) {
                System.err.println("Error generating PDF: " + e.getMessage());
            }

            deleteClassFiles(compiledFilesDirectory);
        }
    }
                
    private static EvaluationResult runEvaluation(String className, Evaluator eval, CompilationFacade facade) {
        EvaluationResult result = new EvaluationResult();
        Optional<Class<?>> clazz = facade.compileAndLoadClass(className);
        if(facade.isSuccessful()) {
            if(clazz.isPresent()) {
                Class<?> clazzClass = clazz.get();
                eval.setClass(clazzClass);
                result = eval.evaluate();
                return result;
            }
        } else {
            System.out.println(className + " does not exist.");
        }
        return null;
    }

    private static void deleteClassFiles(Path directory) {
        File file = new File(directory.toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if(f.getName().endsWith(".class")) {
                    f.delete();
                }
            }
        }
    }

    private static File[] getSubmissions(Path directory) {
        File file = new File(directory.toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                int count = 0;

                for (File f : files) {
                    if (f.isDirectory()) {
                        count++;
                    }
                }

                File[] folders = new File[count];
                int index = 0;
                for (File f : files) {
                    if (f.isDirectory()) {
                        folders[index] = f;
                        index++;
                    }
                }
                return folders;
            }
        }
        return null;
    }

}