package com.assignmentgrader.app;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChatBotSimulationEvaluator implements Evaluator {
    private final Class<?> chatBotSimulationClass;
    private final ByteArrayOutputStream outputStreamCaptor;
    private final PrintStream originalOut = System.out;
    private final Path filePath;

    public ChatBotSimulationEvaluator(Class<?> clazz, Path filePath) {
        this.chatBotSimulationClass = clazz;
        this.outputStreamCaptor = new ByteArrayOutputStream();
        this.filePath = filePath;
    }

    @Override
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();
        result.setClassName("ChatBotSimulation");
        new TestEvaluator().evaluate(result);
        return result;
    }

    private class TestEvaluator {
        private void evaluate(EvaluationResult result) {
            String output = "";
            String fileContent = "";
            try {
                System.setOut(new PrintStream(outputStreamCaptor));
                Method main = chatBotSimulationClass.getMethod("main", String[].class);
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