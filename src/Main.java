import processing.core.PApplet;

public class Main extends PApplet {
	ComplexGraph graph;
	Point last;
	Point first;

	public void setup() {
		size(1200, 600);

		graph = new Exponential(this);
		graph.setComplexSourceRange(-4, 4, -4, 4);
		graph.setComplexTargetRange(-4, 4, -4, 4);
		graph.setSourceImage(loadImage("../assets/graph.png"));
		graph.calculateTargetImage();

	}

	public void draw() {
		background(255);
		graph.draw();

		if (mousePressed) {
			if (last == null) {
				last = new Point(mouseX, mouseY);
			}
			if (first == null) {
				first = new Point(mouseX, mouseY);
			}

			first = new Point(mouseX, mouseY);

			graph.addSpotToGraph(mouseX, mouseY, 10, color(255, 0, 0));

		}
	}

	private class Point {
		private double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
}
