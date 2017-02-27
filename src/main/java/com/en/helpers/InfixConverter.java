package com.en.helpers;


import com.en.core.Operation;

import java.util.*;

/**
 * Created by atrposki on 14-Feb-17.
 */
public class InfixConverter {
    private Map<String,Operation> operationsMap = new HashMap<>();
    private Object leftGrouper;
    private Object rightGrouper;
    public InfixConverter(Map<String,Operation> allOperations, Object leftGrouper, Object rightGrouper){
        this.operationsMap=allOperations;
        this.leftGrouper=leftGrouper;
        this.rightGrouper = rightGrouper;
    }

    public List<String> toPostFix(List<String> infixTokens){
        ArrayList<String> output = new ArrayList<>();
        Deque<Object> stack = new LinkedList<>();

        for (String token : infixTokens) {
            // operator
            if (operationsMap.containsKey(token.toLowerCase().trim())) {
                Operation operation = operationsMap.get(token.toLowerCase().trim());

                if(!stack.isEmpty()){
                    while ((stack.peek() instanceof Operation) && operation.getPrecedence() > ((Operation) stack.peek()).getPrecedence()){
                        output.add(stack.pop().toString());
                    }
                }


                stack.push(operation);
                // left parenthesis
            } else if (token.equals(leftGrouper.toString())) {
                stack.push(leftGrouper);
                // right parenthesis
            } else if (token.equals(rightGrouper.toString())) {
                while (!stack.peek().equals(leftGrouper)){
                    output.add(stack.pop().toString());
                }
                stack.pop();
                // digit
            } else {
                output.add(token);
            }
        }

        while ( ! stack.isEmpty())
            output.add(stack.pop().toString());

        return output;
    }


    public List<String> toPrefix(List<String> expressions){
        List<String> result = toPostFix(expressions);
        Collections.reverse(result);
        return result;
    }
}
