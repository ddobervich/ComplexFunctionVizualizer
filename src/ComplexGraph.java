import org.apache.commons.math3.complex.Complex;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class ComplexGraph {
	private Boundary preImageBoundary, imageBoundary, preImageDisplayBoundary, imageDisplayBoundary;

	private PApplet window;
	private PImage source, target;

	public ComplexGraph(PApplet window) {
		this.window = window;
	}

	// internal image bounds
	public void setComplexSourceRange(double realMin, double realMax, double imaginaryMin, double imaginaryMax) {
		this.preImageBoundary = new Boundary(realMin, imaginaryMin, realMax, imaginaryMax);
	}

	// internal image bounds after transformation
	public void setComplexTargetRange(double realMin, double realMax, double imaginaryMin, double imaginaryMax) {
		this.imageBoundary = new Boundary(realMin, imaginaryMin, realMax, imaginaryMax);
	}

	public void setDisplaySourceRange(int xmin, int ymin, int xmax, int ymax) {
		this.preImageDisplayBoundary = new Boundary(xmin, ymin, xmax, ymax);

		if (source == null) {
			source = new PImage(xmax - xmin, ymax - ymin);
		} else {
			source.resize(xmax - xmin, ymax - ymin);
			// TODO redraw here?
		}
	}

	public void setDisplayTargetRange(int xmin, int ymin, int xmax, int ymax) {
		this.imageDisplayBoundary = new Boundary(xmin, ymin, xmax, ymax);

		if (target == null) {
			target = new PImage(xmax - xmin, ymax - ymin);
		} else {
			target.resize(xmax - xmin, ymax - ymin);
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

		source.loadPixels();
		target.loadPixels();
		for (int i = 0; i < source.pixels.length; i++) {
			Point p = new Point(i % source.width, i / source.width); // (x, y)
																		// coords
																		// of
																		// pixel
																		// ASSUMING
																		// (0,
																		// 0) is
																		// top
																		// left
																		// corner
			// map from pixel space to complex plane
			Point p2 = this.mapRegion(this.preImageDisplayBoundary, this.preImageBoundary, p);

			// map from complex plane through function
			p2 = function(p2);

			// map from function output to pixel space
			p2 = this.mapRegion(this.imageBoundary, this.imageDisplayBoundary, p2);

			// if in target region, set pixel color
			if (this.imageDisplayBoundary.contains(p2)) {
				int targetIndex = (int) p2.y * this.target.width + (int) p2.x;
				try {
					target.pixels[targetIndex] = source.pixels[i];
				} catch (Exception E) {
					System.out.println("Bad coords: " + p2);
				}
			}
		}

		target.updatePixels();
		source.updatePixels();
	}

	public void setSourceImage(PImage inputImage) {
		this.source = inputImage;
		this.setDisplaySourceRange(0, 0, source.width, source.height);
		this.target = new PImage(source.width, source.height);
		this.setDisplayTargetRange(0, 0, target.width, target.height);
	}

	public void setSourceImageBlank(int width, int height) {
		this.source = new PImage(width, height);
		;
		this.setDisplaySourceRange(0, 0, width, height);
		this.target = new PImage(width, height);
		this.setDisplayTargetRange(0, 0, width, height);
	}

	public void draw() {
		window.image(source, 0, 0); // TODO: add fields for these display
									// locations
		window.image(target, source.width, 0);
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
	private double mapRange(double inMin, double inMax, double outMin, double outMax, double val) {
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
		return new Point(mapRange(source.xMin, source.xMax, target.xMin, target.xMax, val.x),
				mapRange(source.yMin, source.yMax, target.yMin, target.yMax, val.y));
	}

	public void addLineToGraph(double x1, double y1, double x2, double y2, int thickness, int color) {
		double m = (y2 - y1) / (x2 - x1);
		for (double x = x1; x <= x2; x++) {
			for (double y = y1; y <= y2; y += m) {
				addSpotToGraph(x, y, thickness, color);
			}
		}
	}

	public void addSpotToGraph(double pixelx, double pixely, int size, int color) {
		for (double x = pixelx - size / 2; x <= pixelx + size / 2; x++) {
			for (double y = pixelx - size / 2; y <= pixelx + size / 2; y++) {
				if (dist(pixelx, pixely, x, y) < size / 2) {
					addPointToGraph(x, y, color);
				}
			}
		}
	}

	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public void addPointToGraph(double pixelx, double pixely, int color) {
		if (source == null) {
			System.err.println("no source image set yet.");
			return;
		}

		Point p = new Point(pixelx, pixely);

		source.loadPixels();
		target.loadPixels();

		// Add point at source image
		// TODO: sort out this casting to int stuff
		// TODO: why? I don't see the problem...
		int sourceIndex = (int) (pixely * source.width + pixelx);
		source.pixels[sourceIndex] = color;

		// map from pixel space to complex plane
		Point p2 = this.mapRegion(this.preImageDisplayBoundary, this.preImageBoundary, p);

		// map from complex plane through function
		p2 = function(p2);

		// map from function output to pixel space
		p2 = this.mapRegion(this.imageBoundary, this.imageDisplayBoundary, p2);

		// if in target region, set pixel color
		if (this.imageDisplayBoundary.contains(p2)) {
			int targetIndex = (int) p2.y * this.target.width + (int) p2.x;
			try {
				target.pixels[targetIndex] = color;
			} catch (Exception E) {
				System.out.println("Bad coords: " + p2);
			}

		}

		target.updatePixels();
		source.updatePixels();
	}

	private class Point {
		private double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return "" + x + ", " + y;
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