package com.en;

import com.en.helpers.ExpressionSplitter;
import com.en.helpers.InfixConverter;
import com.en.helpers.NodeConcatinator;
import lombok.Data;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by atrposki on 14-Feb-17.
 */
public class Calculator {

    private InfixConverter infixConverter;
    private ExpressionSplitter expressionSplitter;
    private NodeConcatinator nodeConcatinator;
    private  Semantic semantic;

    public Calculator(InfixConverter infixConverter, ExpressionSplitter expressionSplitter, NodeConcatinator nodeConcatinator, Semantic semantic) {
        this.infixConverter = infixConverter;
        this.expressionSplitter = expressionSplitter;
        this.nodeConcatinator = nodeConcatinator;
        this.semantic = semantic;
    }

    public Calculator(Semantic semantic){
        this.semantic=semantic;
        Map<String,Operation> operationsMap = new HashMap<>();
        Set<String> operators = new HashSet<>();

        semantic.allOperations.forEach(x->operationsMap.put(x.getExpression(),x));

        operationsMap.keySet().forEach(x->operators.add(x));
        operators.add(semantic.leftGrouper.toString());
        operators.add(semantic.rightGrouper.toString());

        infixConverter = new InfixConverter(operationsMap,semantic.leftGrouper,semantic.rightGrouper);
        expressionSplitter = new ExpressionSplitter(operators,semantic.wordSplitCriteria);
        nodeConcatinator = new NodeConcatinator(semantic);
    }

    public InfixConverter getInfixConverter() {
        return infixConverter;
    }

    public void setInfixConverter(InfixConverter infixConverter) {
        this.infixConverter = infixConverter;
    }

    public ExpressionSplitter getExpressionSplitter() {
        return expressionSplitter;
    }

    public void setExpressionSplitter(ExpressionSplitter expressionSplitter) {
        this.expressionSplitter = expressionSplitter;
    }

    public NodeConcatinator getNodeConcatinator() {
        return nodeConcatinator;
    }

    public void setNodeConcatinator(NodeConcatinator nodeConcatinator) {
        this.nodeConcatinator = nodeConcatinator;
    }


    public  String calculate(String infix){
        List<String> rawTokens = expressionSplitter.split(infix);
        List<String> expressions = semantic.prefixNotation? infixConverter.toPrefix(rawTokens): infixConverter.toPostFix(rawTokens);
        CalculatorNode root = nodeConcatinator.concatNodes(expressions);
        return root.calculate().getValue();
    }



    private static boolean isAplhaNum(char c){
        return Character.isAlphabetic(c)||Character.isDigit(c);
    }

    @Data
    public static class Semantic{
        private boolean prefixNotation;
        private List<Operation> allOperations;
        private Comparator<Operation> operationPrecedenceComparator;
        private Object leftGrouper;
        private Object rightGrouper;
        private BiFunction<Character,Character,Boolean> wordSplitCriteria;

    }

    public static Semantic creteAlgebaricSemantic(){
        Semantic semantic = new Semantic();
        semantic.prefixNotation =true;
        semantic.allOperations=Operation.algebaricOperations;
        semantic.operationPrecedenceComparator=Operation.precedenceComparator;
        semantic.leftGrouper="(";
        semantic.rightGrouper=")";
        semantic.wordSplitCriteria =(l,r)->{

            if(new Character(r).equals('.')){
                return !Character.isDigit(l);
            }else if(new Character(l).equals('.')){
                return !Character.isDigit(r);
            }else {
                return isAplhaNum(l)!=isAplhaNum(r);
            }

        };
        return  semantic;
    }

    public static Semantic creteControllFlowSemantic(){
        Semantic semantic = new Semantic();
        semantic.prefixNotation = false;
        semantic.allOperations=Operation.flowOperations;
        semantic.operationPrecedenceComparator=Operation.precedenceComparator;
        semantic.leftGrouper="";
        semantic.rightGrouper="";
        semantic.wordSplitCriteria =(l,r)->false;
        return  semantic;
    }

}
