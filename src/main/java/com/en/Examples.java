package com.en;

import com.en.core.Calculator;

/**
 * Created by atrposki on 27-Feb-17.
 */
public class Examples {
    public static void main(String[] args){
        Calculator controlFlowCalculator = new Calculator(Calculator.creteControllFlowSemantic());
        String result = controlFlowCalculator.calculate("if 13>1-4*23 " +
            "                                 then 13 is larger  " +
                "                             else 13 is smaller ");
        System.out.println(result); // 13 is larger

        Calculator algebraicCalculator = new Calculator(Calculator.creteAlgebaricSemantic());
        result = algebraicCalculator.calculate("1-4*23"); //-91.0
        System.out.println(result);

        result = algebraicCalculator.calculate("13>1-4*23");//true
        System.out.println(result);
        return;
    }
}
