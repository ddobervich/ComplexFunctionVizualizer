import org.apache.commons.math3.complex.Complex;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class ComplexGraph {
	private Boundary preImageBoundary, imageBoundary, preImageDisplayBoundary,
			imageDisplayBoundary;

	private PApplet window;
	private PImage source, target;

	public ComplexGraph(double a, double b, double c, double d, PApplet window) {
		// TODO What are the four numbers for??

		this.window = window;
	}

	// internal image bounds
	public void setComplexSourceRange(double realMin, double realMax,
			double imaginaryMin, double imaginaryMax) {
		this.preImageBoundary = new Boundary(realMin, imaginaryMin, realMax,
				imaginaryMax);
	}

	// internal image bounds after transformation
	public void setComplexTargetRange(double realMin, double realMax,
			double imaginaryMin, double imaginaryMax) {
		this.imageBoundary = new Boundary(realMin, imaginaryMin, realMax,
				imaginaryMax);
	}

	public void setDisplaySourceRange(int xmin, int ymin, int xmax, int ymax) {
		this.preImageDisplayBoundary = new Boundary(xmin, xmax, ymin, ymax);

		if (source == null) {
			source = new PImage(xmax - xmin, ymax - ymin);
		} else {
			source.resize(xmax - xmin, ymax - ymin);
		}
	}

	public void setDisplayTargetRange(int xmin, int ymin, int xmax, int ymax) {
		this.imageDisplayBoundary = new Boundary(xmin, xmax, ymin, ymax);

		if (target == null) {
			target = new PImage(xmax - xmin, ymax - ymin);
		} else {
			target.resize(xmax - xmin, ymax - ymin);
			// TODO: also re-calculate here?
		}
	}

	public abstract Complex function(Complex in);

	public Point function(Point in) {
		Complex c = function(new Complex(in.x, in.y));
		in.x = c.getReal();
		in.y = c.getImaginary();
		return in;
	}

	public void calculateTargetImage() {
		if (source == null) {
			System.err.println("no source image set yet.");
			return;
		}

		source.loadPixels(); // TODO: double check that this is correct...
		for (int i = 0; i < source.pixels.length; i++) {
			Point p = new Point(i % source.width, i / source.width); // (x, y) coords
																																// of pixel
																																// ASSUMING
																																// (0, 0) is top
																																// left corner
			// map from pixel space to complex plane
			Point p2 = this.mapRegion(this.preImageDisplayBoundary,
					this.preImageBoundary, p);

			// map from complex plane through function
			// map from function output to pixel space
			// if in target region, set pixel color
		}
	}

	public void draw() {
		window.image(source, 0, 0); // TODO: add fields for these display locations
		window.image(target, source.width, 0);
	}

	/***
	 * Convert a complex number to a (x,y) coordinate
	 * 
	 * @param i
	 * @return A point
	 */
	public Point getPointFrom(Complex i) {
		return new Point(i.getReal(), i.getImaginary());
	}

	/***
	 * Convert a coordinate (x,y) to a complex number
	 * 
	 * To be used to perform transformations
	 * 
	 * @param p
	 * @return Complex number
	 */
	public Complex getComplexFrom(Point p) {
		return new Complex(p.x, p.y);
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

		public boolean contains(Point p) {
			return (p.x >= xMin && p.x <= xMax && p.y <= yMax && p.y >= yMin);
		}
	}
}