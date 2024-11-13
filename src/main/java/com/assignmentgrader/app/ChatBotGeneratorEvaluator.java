package com.assignmentgrader.app;

import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;

public class ChatBotGeneratorEvaluator implements Evaluator {
    private int marks = 0;
    private List<String> testResults;

    @Override
    public List<String> runEvaluation(Class<?> chatBotGeneratorClass) {
        if(!chatBotGeneratorClass.getSimpleName().equals("ChatBotGenerator")) {
            System.err.println("Error: Class Name should be 'ChatBotGenerator'.");
        }

        String result;
        testResults = new ArrayList<String>();
        try {
            Method generateChatBotLLM = chatBotGeneratorClass.getMethod("generateChatBotLLM", int.class);
            if (generateChatBotLLM.getReturnType().getSimpleName().equals("ChatBot") && Modifier.isPublic(generateChatBotLLM.getModifiers()) && Modifier.isStatic(generateChatBotLLM.getModifiers())) {
                result = "generateChatBotLLM(): correct return type, access modifier and ChatBot object returned... 3 marks";
                marks += 3;
                testResults.add(result);
                // Test functionality of generateChatBotLLM for different LLMCode values
               //evaluateGenerateChatBotLLMFunctionality(generateChatBotLLM);
            }
        } 
        
        catch (NoSuchMethodException e) {
            System.out.println("Error: Missing required method - " + e.getMessage());
        }

        return testResults;
    }
}
