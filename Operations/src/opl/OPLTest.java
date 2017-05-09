package opl;
import org.junit.Test;

import test.Util;
public class OPLTest {
	@Test
	public void test(){
		Exp e = new Times( new Int(2), 
	    		new Plus( new Int(4), 
	    		new Plus( new Int(5), 
	    		new Times( new Int(5), new Int(4)))));
		Util.val(e.eval());
	    System.out.println(e.print() + " = " + e.eval());
	}
}
