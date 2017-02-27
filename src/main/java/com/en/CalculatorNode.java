package com.en;

/**
 * Created by atrposki on 13-Feb-17.
 */
public interface CalculatorNode {
    Calculator.Semantic getSemantic();
    CalculatorNode getParent();
    CalculatorNode calculate();
    String getValue();
    CalculatorNode[] getChildren();
    CalculatorNode add(CalculatorNode newNode);
}
