import org.apache.commons.math3.complex.Complex;

import processing.core.PApplet;

public class Exponential extends ComplexGraph {

	public Exponential(PApplet window) {
		super(window);
	}

	@Override
	/***
	 * function to visualize: f(z) = z^2
	 */
	public Complex function(Complex in) {
		return in.exp();
	}

}
