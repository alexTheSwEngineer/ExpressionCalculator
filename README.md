# ExpressionCalculator

This is a small but exstensible project for evaluating expressions with c like semantic.
It currently can understand the if/then/else construct as well as the operations: >,<,=,!=,>=,<=,+,-,/,*,&&,||.
This operations set can easilly be extended. The design is based heavily on this clean code talk:
https://www.youtube.com/watch?v=4F72VULWFvc
The main diference being,this design uses parametric polymorphism and relies on java 8 features heavily.

## example:
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