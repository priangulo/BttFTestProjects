package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import operations.Minus;
import operations.Times;

public class Calc {
	public static void print_operation(){
		original();
		operators.add("*");
		System.out.print(" * ");
	}
	
	public static float perform_operation(float exp1, float exp2, String op){
		if(op.equals("*")){
			Operation operation = new Times();
			float result = operation.operation_method(exp1, exp2);
			if(!Float.isNaN(result))
			{
				System.out.println(exp1 + " " + op + " " + exp2 + " = " + result );
			}
			return result;
		}
		return original(exp1, exp2, op);
	}
}
