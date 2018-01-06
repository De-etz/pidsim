import java.util.HashMap;

public class Main {
	
	//Function's attributes to observe
	static final double LOWER = -3; //Lower bound, default -3
	static final double UPPER = 3; //Upper bound, default 3
	static final double INIT = f(LOWER); //Initial y value of the function
	static final double STEP = .01; //Change in x (DON'T GO PAST 1000THS PLACE), default .01
	
	//Gain defaults
	static final double BGAIN = 4.020; //Bang
	static final double PGAIN = -1.978; //Proportional
	static final double IGAIN = .0003; //Integral
	static final double DGAIN = .009; //Derivative
	
	/**
	 * The function that returns the values that should be followed.
	 * @param x Independent variable (Typically time)
	 * @return Dependent variable
	 */
	public static double f(double x) {
		return 10*(-.4*Math.pow(x, 4)-.2*Math.pow(x, 3)+.6*Math.pow(x, 2)+.2*x-.1); //Equation (-.4x^4-.2x^3+.6x^2+.2x-.1)
	}
	
	/**
	 * Bang algorithm to approximate
	 * @param x Independent variable
	 * @return Estimate of f(x)
	 */
	public static double B(double x) {
		//If first value, use initial value and don't look back (Literally, unless you want a NullPointerException)
		if (x == LOWER) {
			return INIT;
		}
		
		//Function and estimate values
		double y1 = pos.get(Math.round((x-STEP)*1000)/1000.0); //Previous value (With decimal correction)
		double y = f(Math.round((x-STEP)*1000)/1000.0); //Target value (With decimal correction)
		double e = y1-y; //Amount of e between previous position and target
		double y2; //Estimated value
		
		//Bang variables
		final double bGain = BGAIN; //Amount to bGain estimate by to match f(x)
		
		//Apply changes and return estimate
		if (e > 0) {
			y2 = y1-bGain;
		} else if (e < 0) {
			y2 = y1+bGain;
		} else {
			y2 = y1;
		}
		return y2;
	}
	
	/**
	 * Proportional algorithm to approximate
	 * @param x Independent variable
	 * @return Estimate of f(x)
	 */
	public static double P(double x) {
		//If first value, use initial value and don't look back (Literally, unless you want a NullPointerException)
		if (x == LOWER) {
			return INIT;
		}
		
		//Function and estimate values
		double y1 = pos.get(Math.round((x-STEP)*1000)/1000.0); //Previous value (With decimal correction)
		double y = f(Math.round((x-STEP)*1000)/1000.0); //Target value (With decimal correction)
		double e = y1-y; //Amount of e between previous position and target
		double y2; //Estimated value
		
		//Proportional variables
		final double pGain = PGAIN;
		
		//Apply changes and return estimate
		y2 = y1 + e*pGain;
		return y2;
	}
	
	/**
	 * Proportional and derivative algorithm to approximate
	 * @param x Independent variable
	 * @return Estimate of f(x)
	 */
	public static double PD(double x) {
		//If first value, use initial value and don't look back (Literally, unless you want a NullPointerException)
		if (x == LOWER) {
			return INIT;
		}
		
		//If second value, use Proportions only, since no change has occurred yet for a derivative to exist
		if (x == LOWER + STEP) {
			return P(x);
		}
		
		//Function and estimate values
		double y1 = pos.get(Math.round((x-STEP)*1000)/1000.0); //Previous value (With decimal correction)
		double y = f(Math.round((x-STEP)*1000)/1000.0); //Target value (With decimal correction)
		double e = y1-y; //Amount of e between previous position and target
		double y2; //Estimated value
		
		//Proportional variables
		final double pGain = PGAIN;
		
		//Derivative variables
		final double dGain = DGAIN;
		double d = ((pos.get(Math.round((x-STEP)*1000)/1000.0))-pos.get(Math.round((x-2*STEP)*1000)/1000.0))/STEP;
		
		//Apply changes and return estimate
		y2 = y1 + e*pGain + d*dGain;
		return y2;
	}

	/**
	 * Proportional and integral algorithm to approximate
	 * @param x Independent variable
	 * @return Estimate of f(x)
	 */
	public static double PI(double x) {
		//If first value, use initial value and don't look back (Literally, unless you want a NullPointerException)
		if (x == LOWER) {
			return INIT;
		}
		
		//If second value, use Proportions only, since no change has occurred yet for a derivative to exist
		if (x == LOWER + STEP) {
			return P(x);
		}
		
		//Function and estimate values
		double y1 = pos.get(Math.round((x-STEP)*1000)/1000.0); //Previous value (With decimal correction)
		double y = f(Math.round((x-STEP)*1000)/1000.0); //Target value (With decimal correction)
		double e = y1-y; //Amount of e between previous position and target
		double y2; //Estimated value
		
		//Proportional variables
		final double pGain = PGAIN;
		
		//Integral variables
		final double iGain = IGAIN;
		double i = 0;
		for (double j = LOWER + STEP; j < x; j = Math.round((j+STEP)*1000)/1000.0) {
			double trap = STEP*((pos.get(Math.round((x-STEP)*1000)/1000.0))+pos.get(Math.round((x-2*STEP)*1000)/1000.0))/2;
			i += trap;
		}

		//Apply changes and return estimate
		y2 = y1 + e*pGain + i*iGain;
		return y2;
	}
	
	/**
	 * Proportional, integral, and derivative algorithm to approximate
	 * @param x Independent variable
	 * @return Estimate of f(x)
	 */
	public static double PID(double x) {
		//If first value, use initial value and don't look back (Literally, unless you want a NullPointerException)
		if (x == LOWER) {
			return INIT;
		}
		
		//If second value, use Proportions only, since no change has occurred yet for a derivative to exist
		if (x == LOWER + STEP) {
			return P(x);
		}
		
		//Function and estimate values
		double y1 = pos.get(Math.round((x-STEP)*1000)/1000.0); //Previous value (With decimal correction)
		double y = f(Math.round((x-STEP)*1000)/1000.0); //Target value (With decimal correction)
		double e = y1-y; //Amount of e between previous position and target
		double y2; //Estimated value
		
		//Proportional variables
		final double pGain = PGAIN;
		
		//Integral variables
		final double iGain = .0003;
		double i = 0;
		for (double j = LOWER + STEP; j < x; j = Math.round((j+STEP)*1000)/1000.0) {
			double trap = STEP*((pos.get(Math.round((x-STEP)*1000)/1000.0))+pos.get(Math.round((x-2*STEP)*1000)/1000.0))/2;
			i += trap;
		}
		
		//Derivative variables
		final double dGain = DGAIN;
		double d = ((pos.get(Math.round((x-STEP)*1000)/1000.0))-pos.get(Math.round((x-2*STEP)*1000)/1000.0))/STEP;

		//Apply changes and return estimate
		y2 = y1 + e*pGain + d*dGain + i*iGain;
		return y2;
	}

	//Approximations
	static final HashMap<Double, Double> pos = new HashMap<Double, Double>(); 
	
	public static void main(String[] args) {
		
		double err = 0;
		
		for (double x = LOWER; x <= UPPER; x=x+STEP) {
			//This just prevents double inaccuracies
			x = Math.round(x*1000)/1000.0;
			
			System.out.printf("%.3f:", x);
			double expected = PI(x);
			double actual = f(x);
			pos.put(x, expected);
			System.out.printf("	E: %.3f", expected);
			System.out.printf(", A: %.3f", actual);
			System.out.printf(", Diff: %.3f%n", expected-actual);
						
			err += Math.abs(expected-actual);
		}
		
		err = err/((UPPER-LOWER+1)/STEP); 
		System.out.printf("Total error: %.5f", + err);
		
	}
}
