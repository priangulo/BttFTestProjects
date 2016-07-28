package base;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.io.BufferedReader;

public class Calc {
	public static List<String> operators = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException{
		print_operation();
		
		while(true)
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("");
			System.out.print("? ");
			String input = bufferedReader.readLine();
			args = input.split(" ");
			
			if(resolve(args)){
				System.out.println("Finished OK");
			}
			else{
				System.out.println("Finished Error");
			}
		}
	}
	
	public static boolean resolve(String args[]){		
		if(args != null && args.length > 2){ 
			int args_length = args.length;
			int i = args_length-1;
			Stack<String> operators_stack = new Stack<String>();
			List<Integer> numbers = new ArrayList<Integer>();
			int operators_length = 0;
			int numbers_length = 0;
			
			//get operators stack
			while(!tryParseInt(args[i]) && i >= 0){
				if (operators.contains(args[i])){
					operators_stack.push(args[i]);
					i--;
				}
				else{
					System.out.println("Unsupported operation ("+ args[i] +")");
					return false;
				}
			}
			operators_length = operators_stack.size();
			
			//get integers operands list
			if(i >= 0){
				try{
					for(int j=0; j <= i; j++){
						numbers.add(Integer.parseInt(args[j]));
					}
					numbers_length = numbers.size();
				}
				catch (NumberFormatException ex){
					System.out.println("Arguments Format Error");
					return false;
				}
			}
			else{
				System.out.println("Not enough arguments");
				return false;
			}
			
			//verify amount of operators vs operands
			if (operators_length != (numbers_length - 1)){
				System.out.println("Not enough arguments");
				return false;
			}
			
			
			//execute operations
			int k = numbers_length - 1;
			float result;
			float exp1 = numbers.get(k-1);
			float exp2 = numbers.get(k);
			String op = operators_stack.pop();
			k = k - 2;
			result = perform_operation(exp1, exp2, op);
			
			while(!operators_stack.isEmpty()){
				exp1 = numbers.get(k);
				exp2 = result;
				op = operators_stack.pop();
				k--;
				result = perform_operation(exp1, exp2, op);
			}
			
			return true;
		}
		else{
			System.out.println("Not enough arguments");
			return false;
		}
	}
	
	
	static boolean tryParseInt(String value) {  
		try {  
		    Integer.parseInt(value);  
		    return true;  
		} 
		catch (NumberFormatException ex) {  
		    return false;  
		}
	}
	
	public static void print_operation(){
		System.out.print("Supported operations:");
	}
	
	public static float perform_operation(float exp1, float exp2, String op){
		return Float.NaN;
	}

	

}
