package com.assignmentgrader.app;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ArrayList;


public class ChatBotEvaluator implements Evaluator {
    private List<String> testResults;
    private int marks = 0;
    @Override
    public List<String> runEvaluation(Class<?> chatBotClass) {
        if(!chatBotClass.getSimpleName().equals("ChatBot")) {
            System.err.println("Error: Class Name should be 'ChatBot'.");
        }

        testResults = new ArrayList<String>();
        evaluateAttributes(chatBotClass);
        evaluateConstructors(chatBotClass);
        evaluateMethods(chatBotClass);
        return testResults;
    }

    private void evaluateAttributes(Class<?> chatBotClass) {
        String result;
        try {
            Field chatBotName = chatBotClass.getDeclaredField("chatBotName");
            if (chatBotName.getType() == String.class && Modifier.isPrivate(chatBotName.getModifiers())) {
                result = "chatBotName: correct type and access modifier... 1 mark";
                marks += 1;
            } else {
                result = "chatBotName: incorrect return type or access modifier type ...";
            }
            testResults.add(result);

            Field numResponsesGenerated = chatBotClass.getDeclaredField("numResponsesGenerated");
            if (numResponsesGenerated.getType() == int.class && Modifier.isPrivate(numResponsesGenerated.getModifiers())) {
                result = "numResponsesGenerated: correct type and access modifier... 1 mark";
                marks += 1;
            } else {
                result = "numResponsesGenerated: incorrect return type or access modifier type ...";
            }
            testResults.add(result);

            Field messageLimit = chatBotClass.getDeclaredField("messageLimit");
            if (messageLimit.getType() == int.class && Modifier.isPrivate(messageLimit.getModifiers())) {
                result = "messageLimit: correct type and access modifier... 3 marks";
                marks += 3;
            } else {
                result = "messageLimit: incorrect return type or access modifier type ...";
            }
            testResults.add(result);

            Field messageNumber = chatBotClass.getDeclaredField("messageNumber");
            if (messageNumber.getType() == int.class && Modifier.isPrivate(messageNumber.getModifiers())) {
                result = "messageNumber: correct type and access modifier... 2 marks";
                marks += 2;
            } else {
                result = "messageNumber: incorrect return type or access modifier type ...";
            }
            testResults.add(result);
        }

        catch (NoSuchFieldException e) {
            System.out.println("Error: Missing required field - " + e.getMessage());
        }
    }

    private void evaluateConstructors(Class<?> chatBotClass) {
        String result;
        try {
            Constructor<?> defaultConstructor = chatBotClass.getConstructor();
            Object defaultInstance = defaultConstructor.newInstance();
            Field chatBotNameField = chatBotClass.getDeclaredField("chatBotName");
            chatBotNameField.setAccessible(true);
            String chatBotName = (String) chatBotNameField.get(defaultInstance);
            if ("ChatGPT-3.5".equals(chatBotName)) {
                result = "ChatBot(): correct default ChatBot object created ... 3 marks";
                marks += 3;
                testResults.add(result);
            }

            Constructor<?> paramConstructor = chatBotClass.getConstructor(int.class);
            Object paramInstance = paramConstructor.newInstance(1);
            String chatBotNameWithCode1 = (String) chatBotNameField.get(paramInstance);
            if ("LLaMa".equals(chatBotNameWithCode1)) { // Assuming LLMCode 1 maps to "LLaMa"
                result = "ChatBot(int): correct ChatBot object created ... 3 marks";
                marks += 3;
            } else {
                result = "ChatBot(int): Overloaded constructor does not set chatBotName correctly for LLMCode 1.";
            }
            testResults.add(result);
        } 
        
        catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e ) {
            System.out.println("Error in constructor evaluation: " + e.getMessage());
        }
    }

    private void evaluateMethods(Class<?> chatBotClass) {
        String result;
        try {
            Method getChatBotName = chatBotClass.getMethod("getChatBotName");
            if (getChatBotName.getReturnType() == String.class && Modifier.isPublic(getChatBotName.getModifiers())) {
                result = "getChatBotName(): correct return type and access modifiers... 1 mark";
                marks += 1;
                testResults.add(result);
            }
    
            Method getNumResponsesGenerated = chatBotClass.getMethod("getNumResponsesGenerated");
            if (getNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getNumResponsesGenerated.getModifiers())) {
                result = "getNumResponsesGenerated(): correct return type and access modifiers... 1 mark";
                marks += 1;
                testResults.add(result);
            }
    
            Method getTotalNumResponsesGenerated = chatBotClass.getMethod("getTotalNumResponsesGenerated");
            if (getTotalNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getTotalNumResponsesGenerated.getModifiers()) && Modifier.isStatic(getTotalNumResponsesGenerated.getModifiers())) {
                result = "getTotalNumResponsesGenerated(): correct return type and access modifiers... 2 marks";
                marks += 2;
                testResults.add(result);
            }
    
            Method getTotalNumMessagesRemaining = chatBotClass.getMethod("getTotalNumMessagesRemaining");
            if (getTotalNumMessagesRemaining.getReturnType() == int.class && Modifier.isPublic(getTotalNumMessagesRemaining.getModifiers()) && Modifier.isStatic(getTotalNumMessagesRemaining.getModifiers())) {
                result = "getTotalNumMessagesRemaining(): correct return type and access modifiers... 3 marks";
                marks += 3;
                testResults.add(result);
            }
    
            Method limitReached = chatBotClass.getMethod("limitReached");
            if (limitReached.getReturnType() == boolean.class && Modifier.isPublic(limitReached.getModifiers()) && Modifier.isStatic(limitReached.getModifiers())) {
                result = "limitReached(): correct return type and access modifiers... 3 marks";
                marks += 3;
                testResults.add(result);
            }
    
            Method generateResponse = chatBotClass.getDeclaredMethod("generateResponse");
            if (generateResponse.getReturnType() == String.class && Modifier.isPrivate(generateResponse.getModifiers())) {
                result = "generateResponse(): correct return type and access modifiers... 5 marks";
                marks += 5;
                testResults.add(result);
            }
    
            Method prompt = chatBotClass.getMethod("prompt", String.class);
            if (prompt.getReturnType() == String.class && Modifier.isPublic(prompt.getModifiers())) {
                result = "generateResponse(): correct return type and access modifiers... 4 marks";
                marks += 4;
                testResults.add(result);
            }
    
            Method toStringMethod = chatBotClass.getMethod("toString");
            if (toStringMethod.getReturnType() == String.class && Modifier.isPublic(toStringMethod.getModifiers())) {
                result = "toString(): correct return type and access modifiers... 4 marks";
                marks += 4;
                testResults.add(result);
            }
        } 
        
        catch (NoSuchMethodException e) {
            System.out.println("Error: Missing required method - " + e.getMessage());
        }
    }
}
