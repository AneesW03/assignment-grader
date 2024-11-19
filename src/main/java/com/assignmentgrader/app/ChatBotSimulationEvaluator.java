package com.assignmentgrader.app;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Evaluator for testing the ChatBotSimulation class.
 * Validates the class's functionality based on its output and the provided specifications.
 */
public class ChatBotSimulationEvaluator implements Evaluator {
    private Class<?> chatBotSimulationClass;
    private ByteArrayOutputStream outputStreamCaptor;
    private PrintStream originalOut;
    private Path filePath;
    private boolean passed;

    /**
     * Constructs a new {@code ChatBotSimulationEvaluator}.
     *
     * @param filePath The path of the ChatBotSimulation source file.
     */
    public ChatBotSimulationEvaluator(Path filePath) {
        this.outputStreamCaptor = new ByteArrayOutputStream();
        this.filePath = filePath;
        this.passed = true;
        this.originalOut = System.out;
    }

    @Override
    public void setClass(Class<?> clazz) {
        this.chatBotSimulationClass = clazz;
    }

    @Override
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();
        result.setClassName("ChatBotSimulation");
        new TestEvaluator().evaluate(result);
        return result;
    }

    @Override
    public boolean isPassed() {
        return this.passed;
    }

    /**
     * Evaluates the output and functionality of the ChatBotSimulation class.
     */
    private class TestEvaluator {
        private void evaluate(EvaluationResult result) {
            String output = "";
            String fileContent = "";
            try {
                System.setOut(new PrintStream(outputStreamCaptor));
                Method main = chatBotSimulationClass.getDeclaredMethod("main", String[].class);
                main.invoke(null, (Object) new String[]{});
                output = outputStreamCaptor.toString();
                System.setOut(originalOut);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("ChatBotSimulation: Error - " + e.getMessage());
            } 

            if (output.contains("Hello World")) {
                result.addTestResults(1, "Prints Hello World.");
            }

            try {
                fileContent = Files.readString(filePath);
            } catch (IOException e) {
                System.err.println("Error - " + e.getMessage());
            }
            
            if (fileContent.contains("new ChatBotPlatform();")) {
                result.addTestResults(1, "ChatBotPlatform object declared and initialized.");
            }

            if (fileContent.contains("addChatBot")) {
                result.addTestResults(2, "Adds ChatBot objects to ChatBotPlatform.");
            }

            if (fileContent.contains("getChatBotList()")) {
                result.addTestResults(2, "Prints list of ChatBots in ChatBotPlatform.");
            }

            if (fileContent.contains("interactWithBot")) {
                    result.addTestResults(4, "Interacts with ChatBots.");
            }

            if (fileContent.contains("getChatBotList());")) {
                result.addTestResults(2, "Prints final list of ChatBots in ChatBotPlatform.");
            }
        }
    }
}