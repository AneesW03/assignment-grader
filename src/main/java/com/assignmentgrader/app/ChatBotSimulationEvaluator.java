package com.assignmentgrader.app;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Evaluator for testing the `ChatBotSimulation` class.
 * Simulates the execution of a main method in the class and evaluates its behavior.
 * 
 * Criteria for evaluation:
 * - Output:
 *   - Contains specific strings, such as "Hello World".
 * - Code content:
 *   - Contains expected method calls (e.g., `addChatBot`, `getChatBotList()`).
 * - Interaction:
 *   - Demonstrates interactions with the `ChatBotPlatform`.
 */
public class ChatBotSimulationEvaluator extends EvaluatorTemplate {
    private ByteArrayOutputStream outputStreamCaptor;
    private PrintStream originalOut;
    private Path filePath;

    public ChatBotSimulationEvaluator(Path filePath) {
        this.outputStreamCaptor = new ByteArrayOutputStream();
        this.filePath = filePath;
        this.originalOut = System.out;
    }

    @Override
    protected void evaluateMethods() {
        String output = "";
        String fileContent = "";
        try {
            System.setOut(new PrintStream(outputStreamCaptor));
            Method main = clazz.getDeclaredMethod("main", String[].class);
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
