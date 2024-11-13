package com.assignmentgrader.app;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class ChatBotPlatformEvaluator implements Evaluator {
    private int marks = 0;
    private List<String> testResults;

    @Override
    public List<String> runEvaluation(Class<?> chatBotPlatformClass) {
        if(!chatBotPlatformClass.getSimpleName().equals("ChatBotPlatform")) {
            System.err.println("Error: Class Name should be 'ChatBotPlatform'.");
        }

        testResults = new ArrayList<String>();
        evaluateAttributes(chatBotPlatformClass);
        evaluateConstructors(chatBotPlatformClass);
        evaluateMethods(chatBotPlatformClass);
        return testResults;
    }

    private void evaluateAttributes(Class<?> chatBotPlatformClass) {
        String result;
        try {
            Field botsField = chatBotPlatformClass.getDeclaredField("bots");
            if (botsField.getType() == ArrayList.class && Modifier.isPrivate(botsField.getModifiers())) {
                result = "bots: correct type and access modifier... 2 marks";
                marks += 2;
                testResults.add(result);
            }
        } 
        
        catch (NoSuchFieldException e) {
            System.out.println("Error: Missing required field - " + e.getMessage());
        }
    }

    private void evaluateConstructors(Class<?> chatBotPlatformClass) {
        String result;
        try {
            Constructor<?> constructor = chatBotPlatformClass.getConstructor();
            Object instance = constructor.newInstance();

            Field botsField = chatBotPlatformClass.getDeclaredField("bots");
            botsField.setAccessible(true);
            Object botsValue = botsField.get(instance);
            if (botsValue instanceof ArrayList && ((ArrayList<?>) botsValue).isEmpty()) {
                result = "ChatBotPlatform(): correctly initialized bot collection... 2 marks";
                marks += 2;
                testResults.add(result);
            }
        } 
        
        catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e ) {
            System.out.println("Error in constructor evaluation: " + e.getMessage());
        }
    }

    private void evaluateMethods(Class<?> chatBotPlatformClass) {
        String result;
        try {
            Method addChatBotMethod = chatBotPlatformClass.getMethod("addChatBot", int.class);
            if (addChatBotMethod.getReturnType() == boolean.class && Modifier.isPublic(addChatBotMethod.getModifiers())) {
                result = "addChatBot(int): correct return type and access modifier... 5 marks";
                marks += 5;
                testResults.add(result);
            }
           
            Method getChatBotListMethod = chatBotPlatformClass.getMethod("getChatBotList");
            if (getChatBotListMethod.getReturnType() == String.class && Modifier.isPublic(getChatBotListMethod.getModifiers())) {
                result = "getChatBotList(): correct return type and access modifier... 6 marks";
                marks += 6;
                testResults.add(result);
            }

            Method interactWithBotMethod = chatBotPlatformClass.getMethod("interactWithBot", int.class, String.class);
            if (interactWithBotMethod.getReturnType() == String.class && Modifier.isPublic(interactWithBotMethod.getModifiers())) {
                result = "interactWithBot(int, String): correct return type and access modifier";
                marks += 5;
                testResults.add(result);
            }   
        } 
        
        catch (NoSuchMethodException e) {
            System.out.println("Error: Missing required method - " + e.getMessage());
        }
    }

}
