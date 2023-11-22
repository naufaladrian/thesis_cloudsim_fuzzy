package org.cloudbus.cloudsim.power;



/**
 * Type of "Grafik fungsi keanggotaan"		 
 */
public enum EMembershipFunction {
	LINEAR_UP,
	LINEAR_DOWN,
	TRIANGLE,
	TRAPEZIUM;			
	
	/**
	 * Calculate membership degree on "liner-up" curve type.
	 * @param a Left bound
	 * @param b Right bound
	 * @param val The value being calculated
	 * @return membership degree 
	 */
	public final static double linearUp(double a, double b, double val) {
		return (val-a)/(b-a);
	}
	
	/**
	 * Calculate membership degree on "liner-down" curve type.
	 * @param a Left bound
	 * @param b Right bound
	 * @param val The value being calculated
	 * @return membership degree 
	 */
	public final static double linearDown(double a, double b, double val) {
		return (b-val)/(b-a);
	}
	
	/**
	 * Calculate membership degree on "liner-up" curve type.
	 * @param a Left bound
	 * @param b Middle bound
	 * @param c Right bound 
	 * @param val The value being calculated
	 * @return membership degree
	 * @throws ArithmeticException When {@link ThemeClient} the <code>val</code> entered not in the range of a-c 
	 */
	public final static double triangle(double a, double b, double c, double val) {
		if (val < a || val > c) {
			throw new ArithmeticException("The specified value for the curve is out of bound");
		}
		if (val < b) {
			// Must be entering here if (a <= val <= b)
			return linearUp(a, b, val);
		} else {
			// Must be entering here if (b <= val <= c)
			return linearDown(b, c, val);
		} 			
		
	}
	
	public final static double trapezium(double a, double b, double c, double d, double val) {		
		double start = Double.isNaN(a) ? b : a;
		double end = Double.isNaN(d) ? c : d;
		if (val < start || val > end) {
			throw new ArithmeticException("The specified value for the curve is out of bound");
		}
		if (val < b) {
			// Must be entering here if (a <= val <= b)
			return linearUp(a, b, val);
		} else if (val < c) {
			// Must be entering here if (b <= val <= c)
			return 1;
		} else {
			// Must be entering here if (c <= val <= d)
			return linearDown(c, d, val);
		}
	}
}