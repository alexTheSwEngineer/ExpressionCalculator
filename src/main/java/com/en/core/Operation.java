package com.en.core;

import com.en.helpers.BinaryOperationBuilder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by atrposki on 13-Feb-17.
 */
@Data
public class Operation {
    private Function<List<String>,String> calculate;
    private int precedence;
    private String expression;
    private  int argumentCount;

    public Operation(String expression,int argumentCount,int precedence,Function<List<String>, String> calculate ) {
        this.calculate = calculate;
        this.precedence = precedence;
        this.expression = expression;
        this.argumentCount=argumentCount;
    }

    public String calculate(List<String> arguments){
        return calculate.apply(arguments);
    }

    public String toString(){
        return expression;
    }

    public static Comparator<Operation> precedenceComparator=(l,r)->Integer.compare(l.argumentCount,r.argumentCount);


    public static List<Operation> algebaricOperations;
    public static List<Operation> flowOperations;

    static {
        algebaricOperations = new ArrayList<>();
        BinaryOperationBuilder<Double,Double> numericOperation = new BinaryOperationBuilder<>(x-> Double.parseDouble(x));
        BinaryOperationBuilder<Double,Boolean> compareOperation = new BinaryOperationBuilder<>(x-> Double.parseDouble(x));
        BinaryOperationBuilder<Boolean,Boolean> booleanOperation = new BinaryOperationBuilder<>(x-> Boolean.parseBoolean(x));

        algebaricOperations.add(numericOperation.forExpression("*")
                                          .forFunction((l,r)->l*r)
                                          .withPrecedence(1)
                                          .build());
        algebaricOperations.add(numericOperation.forExpression("/")
                                            .forFunction((l,r)->l/r)
                                            .withPrecedence(2)
                                            .build());
        algebaricOperations.add(numericOperation.forExpression("+")
                                            .forFunction((l,r)->l+r)
                                            .withPrecedence(3)
                                            .build());
        algebaricOperations.add(numericOperation.forExpression("-")
                                            .forFunction((l,r)->l-r)
                                            .withPrecedence(4)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression("<")
                                            .forFunction((l,r)->l<r)
                                            .withPrecedence(5)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression(">")
                                            .forFunction((l,r)->l>r)
                                            .withPrecedence(6)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression("<=")
                                            .forFunction((l,r)->l<=r)
                                            .withPrecedence(7)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression(">=")
                                            .forFunction((l,r)->l>=r)
                                            .withPrecedence(8)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression("=")
                                            .forFunction((l,r)->l.equals(r))
                                            .withPrecedence(9)
                                            .build());
        algebaricOperations.add(compareOperation.forExpression("!=")
                                            .forFunction((l,r)->l!=r)
                                            .withPrecedence(10)
                                            .build());
        algebaricOperations.add(booleanOperation.forExpression("&&")
                                            .forFunction((l,r)->l&&r)
                                            .withPrecedence(11)
                                            .build());
        algebaricOperations.add(booleanOperation.forExpression("||")
                                            .forFunction((l,r)->l||r)
                                            .withPrecedence(12)
                                            .build());

        flowOperations=new ArrayList<>();
        flowOperations.add(new Operation("if ",3,101,x->{
            Calculator calculator = new Calculator(Calculator.creteAlgebaricSemantic());
            String condition = calculator.calculate(x.get(0));
            if(Boolean.parseBoolean(condition)){
                return x.get(1);
            }

            return x.get(2);
        }));

        flowOperations.add(new Operation("then ",1,100,x->x.get(0)));
        flowOperations.add(new Operation("else ",1,100,x->x.get(0)));
        flowOperations.add(new Operation("elseif ",3,100,x->{
            Calculator calculator = new Calculator(Calculator.creteAlgebaricSemantic());
            String condition = calculator.calculate(x.get(0));
            if(Boolean.parseBoolean(condition)){
                return x.get(1);
            }

            return x.get(2);
        }));
    }


    /*

Multiplicative	 left to right	 *  /  %
Additive	 left to right	     +  -
Relational	 left to right	     <  >  <=  >=
Equality	 left to right	     ==  !=
Logical AND	 left to right	     &&
Logical OR	 left to right	     ||
*/
}
