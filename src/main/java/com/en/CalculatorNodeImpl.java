package com.en;

import java.util.*;
import java.util.function.Function;

/**
 * Created by atrposki on 13-Feb-17.
 */
public class CalculatorNodeImpl implements CalculatorNode {
    private CalculatorNode parent;
    private String expression;
    private CalculatorNode[] children;
    private Function<CalculatorNode[],CalculatorNode> calculateImpl;
    private Calculator.Semantic semantic;


    private CalculatorNodeImpl(Calculator.Semantic semantic,CalculatorNode parent, String expression, int childrenCount, Function<CalculatorNode[], CalculatorNode> calculateImpl) {
        this.semantic=semantic;
        this.parent = parent;
        this.expression = expression;
        this.children = new CalculatorNode[childrenCount];
        this.calculateImpl = calculateImpl;
    }

    public CalculatorNodeImpl(CalculatorNode parent, Operation operation){
        this.semantic=parent.getSemantic();
        this.parent=parent;
        this.children =new CalculatorNode[operation.getArgumentCount()];
        this.expression=operation.getExpression();
        this.calculateImpl=(arguments)->{
            List<String> argumentsAsString = getValue(arguments);
            if(semantic.isPrefixNotation()){
                Collections.reverse(argumentsAsString);
            }
            String result = operation.calculate(argumentsAsString);
            return asResultNode(result,this.parent);
        };
    }

    private CalculatorNodeImpl(String result,CalculatorNode parent) {
        this.semantic=parent.getSemantic();
        this.parent=parent;
        this.expression=result;
        this.children=new CalculatorNode[0];
        this.calculateImpl=x->this;
    }


    @Override
    public Calculator.Semantic getSemantic() {
        return this.semantic;
    }

    @Override
    public CalculatorNode getParent() {
        return parent;
    }

    @Override
    public CalculatorNode calculate() {
        return calculateImpl.apply(children);
    }

    @Override
    public CalculatorNode add(CalculatorNode newNode) {
        for (int i = 0; i< children.length; i++){
            if(children[i]==null){
                children[i]=newNode;
                return newNode;
            }
        }
        return  parent.add(newNode);
    }

    @Override
    public String getValue(){
        return expression;
    }


    private static List<String> getValue(CalculatorNode[] arguments){
        ArrayList<String> result = new ArrayList<String>();
        for (CalculatorNode argument :arguments) {
            result.add(argument.calculate().getValue());
        }

        return result;
    }

    public static CalculatorNode asResultNode(String result,CalculatorNode parent){
        return new CalculatorNodeImpl(result,parent);
    }

    public static CalculatorNodeImpl root(Calculator.Semantic semantic){
        return new CalculatorNodeImpl(semantic,null,"",1,x->{
            return x[0].calculate();
        });
    }

    @Override
    public CalculatorNode[] getChildren()
    {
        return children;

    }


}
