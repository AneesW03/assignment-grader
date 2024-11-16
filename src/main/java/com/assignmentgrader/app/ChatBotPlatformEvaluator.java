package com.assignmentgrader.app;

import java.lang.reflect.*;
import java.util.ArrayList;

public class ChatBotPlatformEvaluator implements Evaluator {
    private final Class<?> chatBotPlatformClass;

    public ChatBotPlatformEvaluator(Class<?> clazz) {
        this.chatBotPlatformClass = clazz;
    }

    @Override
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();
        result.setClassName("ChatBotPlatform");
        new AttributeEvaluator().evaluate(result);
        new ConstructorEvaluator().evaluate(result);
        new MethodEvaluator().evaluate(result);
        return result;
    }

    private class AttributeEvaluator {
        private void evaluate(EvaluationResult result) {
            try {
                Field botsField = chatBotPlatformClass.getDeclaredField("bots");
                if (botsField.getType() == ArrayList.class && Modifier.isPrivate(botsField.getModifiers())) {
                    result.addTestResults(2, "bots: Correct type and access modifier.");
                } else {
                    result.addTestResults(0, "bots: Incorrect type or access modifier.");
                }
            } catch (NoSuchFieldException e) {
                System.out.println("bots: Error - " + e.getMessage());
            }
        }
    }

    private class ConstructorEvaluator {
        private void evaluate(EvaluationResult result) {
            try {
                Constructor<?> constructor = chatBotPlatformClass.getConstructor();
                Object instance = constructor.newInstance();
    
                Field botsField = chatBotPlatformClass.getDeclaredField("bots");
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
    }

    private class MethodEvaluator {
        private void evaluate(EvaluationResult result) {
            try {
                Method addChatBotMethod = chatBotPlatformClass.getMethod("addChatBot", int.class);
                if (addChatBotMethod.getReturnType() == boolean.class && Modifier.isPublic(addChatBotMethod.getModifiers())) {
                    result.addTestResults(5, "addChatBot(int): Correct return type and access modifier");
                } else {
                    result.addTestResults(2, "addChatBot(int): Incorrect return type or access modifier.");
                }
            } catch (NoSuchMethodException e) {
                System.err.println("addChatBot(int LLMCode): Error - " + e.getMessage());
            }

            try {
                Method getChatBotListMethod = chatBotPlatformClass.getMethod("getChatBotList");
                if (getChatBotListMethod.getReturnType() == String.class && Modifier.isPublic(getChatBotListMethod.getModifiers())) {
                    result.addTestResults(6, "getChatBotList(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "getChatBotList(): Incorrect return type or access modifier.");
                }
            } catch (NoSuchMethodException e) {
                System.err.println("getChatBotList(): Error - " + e.getMessage());
            }

            try {
                Method interactWithBotMethod = chatBotPlatformClass.getMethod("interactWithBot", int.class, String.class);
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

}
