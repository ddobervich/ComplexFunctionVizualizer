import org.apache.commons.math3.complex.Complex;

import processing.core.PApplet;

public class Power extends ComplexGraph {
	private int power;

	public Power(PApplet window, int n) {
		super(window);
		power = n;
	}

	@Override
	/***
	 * function to visualize: f(z) = z^n
	 */
	public Complex function(Complex in) {
		return in.pow(power);
	}

}
