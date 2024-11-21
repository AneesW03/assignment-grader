package com.assignmentgrader.app;
import java.lang.reflect.*;
import java.util.ArrayList;

/**
 * Evaluator for testing the `ChatBotPlatform` class.
 * Validates the platform's ability to manage and interact with chatbots.
 * 
 * Criteria for evaluation:
 * - Attributes:
 *   - `bots`: Correct type (ArrayList) and access modifier (private).
 * - Constructors:
 *   - Default constructor initializes the `bots` collection as empty.
 * - Methods:
 *   - `addChatBot(int)`: Adds a chatbot to the platform.
 *   - `getChatBotList()`: Returns a list of chatbots as a formatted string.
 *   - `interactWithBot(int, String)`: Allows interaction with a specific chatbot.
 */
public class ChatBotPlatformEvaluator extends EvaluatorTemplate {
    @Override 
    protected void evaluateAttributes() {
        try {
            Field botsField = clazz.getDeclaredField("bots");
            if (botsField.getType() == ArrayList.class && Modifier.isPrivate(botsField.getModifiers())) {
                result.addTestResults(2, "bots: Correct type and access modifier.");
            } else {
                result.addTestResults(0, "bots: Incorrect type or access modifier.");
            }
        } catch (NoSuchFieldException e) {
            System.out.println("bots: Error - " + e.getMessage());
        }
    }

    @Override
    protected void evaluateConstructors() {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();

            Field botsField = clazz.getDeclaredField("bots");
            botsField.setAccessible(true);
            Object botsValue = botsField.get(instance);
            if (botsValue instanceof ArrayList && ((ArrayList<?>) botsValue).isEmpty()) {
                result.addTestResults(2, "ChatBotPlatform(): Correctly initializes the bots collection.");
            } else {
                result.addTestResults(0, "ChatBotPlatform(): Incorrectly initialized bots collection.");
            }
        } catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e ) {
            System.out.println("ChatBotPlatform(): Error - " + e.getMessage());
        }
    }

    @Override
    protected void evaluateMethods() {
        try {
            Method addChatBotMethod = clazz.getMethod("addChatBot", int.class);
            if (addChatBotMethod.getReturnType() == boolean.class && Modifier.isPublic(addChatBotMethod.getModifiers())) {
                result.addTestResults(5, "addChatBot(int): Correct return type and access modifier");
            } else {
                result.addTestResults(0, "addChatBot(int): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("addChatBot(int LLMCode): Error - " + e.getMessage());
        }

        try {
            Method getChatBotListMethod = clazz.getMethod("getChatBotList");
            if (getChatBotListMethod.getReturnType() == String.class && Modifier.isPublic(getChatBotListMethod.getModifiers())) {
                result.addTestResults(6, "getChatBotList(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "getChatBotList(): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("getChatBotList(): Error - " + e.getMessage());
        }

        try {
            Method interactWithBotMethod = clazz.getMethod("interactWithBot", int.class, String.class);
            if (interactWithBotMethod.getReturnType() == String.class && Modifier.isPublic(interactWithBotMethod.getModifiers())) {
                result.addTestResults(5, "interactWithBot(int, String): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "interactWithBot(int, String): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("interactWithBot(): Error - " + e.getMessage());
        }
    }
}
