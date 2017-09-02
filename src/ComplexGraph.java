import org.apache.commons.math3.complex.Complex;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class ComplexGraph {
	private Boundary preImageBoundary, imageBoundary, preImageDisplayBoundary,
			imageDisplayBoundary;

	private PApplet window;
	private PImage source, target;

	public ComplexGraph(double a, double b, double c, double d, PApplet window) {
		this.window = window;

	}

	public void setComplexSourceRange(double realMin, double realMax,
			double imaginaryMin, double imaginaryMax) {
		this.preImageBoundary = new Boundary(realMin, imaginaryMin, realMax,
				imaginaryMax);
	}

	public void setComplexTargetRange(double realMin, double realMax,
			double imaginaryMin, double imaginaryMax) {
		this.imageBoundary = new Boundary(realMin, imaginaryMin, realMax,
				imaginaryMax);
	}

	public void setDisplaySourceRange(double xmin, double ymin, double xmax,
			double ymax) {
		this.preImageDisplayBoundary = new Boundary(xmin, xmax, ymin, ymax);
	}
	
	public void setDisplayTargetRange(double xmin, double ymin, double xmax,
			double ymax) {
		this.imageDisplayBoundary = new Boundary(xmin, xmax, ymin, ymax);
	}

	public abstract Complex function(Complex in);

	public void draw() {

	}

	/***
	 * Maps val in range [inMin, inMax] to a value in [outMin, outMax]
	 * 
	 * @param inMin
	 * @param inMax
	 * @param outMin
	 * @param outMax
	 * @param val
	 * @return
	 */
	private double mapRange(double inMin, double inMax, double outMin,
			double outMax, double val) {
		return ((val - inMin) / (inMax - inMin)) * (outMax - outMin) + outMin;
	}

	/***
	 * Maps a Point in source region to a Point in the target region
	 * 
	 * @param source
	 * @param target
	 * @param val
	 * @return
	 */
	private Point mapRegion(Boundary source, Boundary target, Point val) {
		return new Point(
				mapRange(source.xMin, source.xMax, target.xMin, target.xMax, val.x),
				mapRange(source.yMin, source.yMax, target.yMin, target.yMax, val.y));
	}

	private class Point {
		private double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	private class Boundary {
		private double xMin, xMax, yMin, yMax;

		public Boundary(double xMin, double yMin, double xMax, double yMax) {
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
		}
	}
}