package opl;

public class OPL {

	public static void main(String[] args) {
	    Exp e = new Times( new Int(2), 
	    		new Plus( new Int(4), 
	    		new Plus( new Int(5), 
	    		new Times( new Int(5), new Int(4)))));
	    System.out.println(e.print() + " = " + e.eval());
	}
}
