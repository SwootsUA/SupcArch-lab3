package task;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Function;

import org.jppf.node.protocol.AbstractTask;

public class LuckyTicketsTask extends AbstractTask<String> {
	private int n;
	private boolean precise;
	
	public LuckyTicketsTask(int n, boolean precise) {
		this.n = n;
		this.precise = precise;
	}
	
	@Override
	public void run() {
		// is ticket lucky = sum of first half numbers is equal to sum in second half numbers
		
		BigInteger result;
		
		if(precise) {
			System.out.println("Starting bruteForce method");
			result = bruteForce(n);
			System.out.println("Completed bruteForce method");
		} else {
			System.out.println("Starting integralSolve method");
			result = integralSolve(n);
			System.out.println("Completed integralSolve method");
		}
		
		System.out.println(this.getId() + " completed with result " + result.toString());
		setResult(result.toString());
	}
	
	private BigInteger bruteForce(int n) {
		BigInteger result = BigInteger.valueOf(0);
		int ticketLength = 2 * n;
		
		BigInteger limit = BigInteger.valueOf(10).pow(ticketLength);
		
		for (BigInteger i = BigInteger.ZERO; i.compareTo(limit) < 0; i = i.add(BigInteger.ONE)) {
			if (isLucky(i, ticketLength)) {
				result = result.add(BigInteger.ONE);
			}
		}
		
		return result;
	}
	
	private static final MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
	
	private BigInteger integralSolve(int n) {
		BigDecimal a = BigDecimal.ZERO;
		BigDecimal b = BigDecimal.valueOf(Math.PI);
		long numIntervals = 3100000; // bigger -> slower -> more precise
		
		Function<BigDecimal, BigDecimal> func = (x) -> integrand(x, n);
		BigDecimal result = integrate(func, a, b, numIntervals).divide(BigDecimal.valueOf(Math.PI), mc);
		return result.setScale(0, RoundingMode.HALF_UP).toBigInteger();
	}
	
	static private boolean isLucky(BigInteger ticket, int length) {
		String ticketString = String.format("%0" + length + "d", ticket);
		
		String firstHalf = ticketString.substring(0, length / 2);
		String secondHalf = ticketString.substring(length / 2);
		
		if (firstHalf.equals(secondHalf)) {
			return true;
		}
		
		int sum1 = 0, sum2 = 0;
		
		for (int i = 0; i < length / 2; i++) {
			sum1 += Character.getNumericValue(firstHalf.charAt(i));
			sum2 += Character.getNumericValue(secondHalf.charAt(i));
		}
		
		return sum1 == sum2;
	}
	
	public static BigDecimal integrate(Function<BigDecimal, BigDecimal> func, BigDecimal a, BigDecimal b, long n) {
		if (n % 2 != 0) {
			n++; // Ensure n is even
		}
		
		BigDecimal h = b.subtract(a).divide(BigDecimal.valueOf(n), mc);
		BigDecimal sum = func.apply(a).add(func.apply(b));
		
		for (long i = 1; i < n; i += 2) {
			BigDecimal x = a.add(h.multiply(BigDecimal.valueOf(i)));
			sum = sum.add(func.apply(x).multiply(BigDecimal.valueOf(4)));
		}
		for (long i = 2; i < n - 1; i += 2) {
			BigDecimal x = a.add(h.multiply(BigDecimal.valueOf(i)));
			sum = sum.add(func.apply(x).multiply(BigDecimal.valueOf(2)));
		}
		
		return sum.multiply(h).divide(BigDecimal.valueOf(3), mc);
	}
	
	public static BigDecimal integrand(BigDecimal x, int n) {
		if (x.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal sinMx = BigDecimal.valueOf(Math.sin(x.doubleValue() * 10));
		BigDecimal sinX = BigDecimal.valueOf(Math.sin(x.doubleValue()));
		
		BigDecimal result = sinMx.divide(sinX, mc).pow(2 * n, mc);
		return result;
	}
}
