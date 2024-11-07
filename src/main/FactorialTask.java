package main;

import java.math.BigInteger;
import org.jppf.node.protocol.AbstractTask;

public class FactorialTask extends AbstractTask<Integer> {
	private long n;
	
	public FactorialTask(long n) {
		this.n = n;
	}
	
	@Override public void run() {
		BigInteger fact = BigInteger.valueOf(1);
		
		for (long i = 2; i <= n; ++i) {
			fact = fact.multiply(BigInteger.valueOf(i));
		}
		
		int result = fact.toString().length();
		System.out.println(this.getId() + " completed with result digit count " + result);
		setResult(result);
	}
}