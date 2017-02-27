package com.en.helpers;

import com.en.Operation;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by atrposki on 14-Feb-17.
 */
public class BinaryOperationBuilder<T,Q>  {

    String expression;
    int precedence;
    Function<String,T> parser;
    BiFunction<T,T,Q> calculate;

    public BinaryOperationBuilder(BinaryOperationBuilder other) {
        this.expression = other.expression;
        this.precedence = other.precedence;
        this.parser = other.parser;
        this.calculate = other.calculate;
    }


    public String getExpression() {
        return expression;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Function<String, T> getParser() {
        return parser;
    }

    public BiFunction<T, T, Q> getCalculate() {
        return calculate;
    }

    public BinaryOperationBuilder(Function<String,T> parser){
        this.parser=parser;
    }

    public  BinaryOperationBuilder<T,Q> forExpression(String expression){
        this.expression=expression;
        return  new BinaryOperationBuilder<T, Q>(this);
    }
    public  BinaryOperationBuilder<T,Q> withPrecedence(int precedence){
        this.precedence=precedence;
        return  new BinaryOperationBuilder<T, Q>(this);
    }

    public  BinaryOperationBuilder<T,Q> forFunction(BiFunction<T,T,Q> calculate){
        this.calculate=calculate;
        return  new BinaryOperationBuilder<T, Q>(this);
    }

    public Operation build(){
        return new Operation(expression,2,precedence,(arguments)-> {
            T arg1=parser.apply(arguments.get(0));
            T arg2 =parser.apply(arguments.get(1));
            Q result=   calculate.apply(arg1,arg2);
            return result.toString();
        });
    }
}
