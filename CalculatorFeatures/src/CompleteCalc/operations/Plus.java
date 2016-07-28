package operations; 
import base.Operation; 

public  class  Plus  implements Operation {
	

	@Override
	public float operation_method(float exp1, float exp2) {
		return exp1 + exp2;
	}


}
