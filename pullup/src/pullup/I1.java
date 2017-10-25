package pullup;

public interface I1 {
	int one();
	
	static int K1 = I2.two()*I1.K0;
	static int K0 = 22;

}
