package com.en.helpers;

import com.en.Calculator;
import com.en.CalculatorNode;
import com.en.CalculatorNodeImpl;
import com.en.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atrposki on 14-Feb-17.
 */
public class NodeConcatinator {
    private final Calculator.Semantic semantic;
    Map<String, Operation> operationsMap= new HashMap<>();
    public NodeConcatinator(Calculator.Semantic semantic)
    {
        this.semantic = semantic;
        operationsMap = new HashMap<>();
        semantic.getAllOperations().forEach(x->operationsMap.put(x.getExpression(),x));
    }


    public CalculatorNode concatNodes(List<String> expressions){
        CalculatorNode root = CalculatorNodeImpl.root(semantic);
        CalculatorNode curentNode = root;
        for (String expression : expressions) {
            Operation operation = operationsMap.get(expression);
            final CalculatorNode parent = curentNode;
            if(operation==null){
                curentNode=curentNode.add(CalculatorNodeImpl.asResultNode(expression,parent));
            }else {
                curentNode = curentNode.add(new CalculatorNodeImpl(parent,operation));
            }
        }

        return root;
    }
}
