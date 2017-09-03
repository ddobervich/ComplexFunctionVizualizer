import processing.core.PApplet;

public class Main extends PApplet {
	ComplexGraph graph;

	public void setup() {
		size(1200, 600);

		graph = new Square(this);
		graph.setComplexSourceRange(-2, 2, -2, 2);
		graph.setComplexTargetRange(-4, 4, -4, 4);
		graph.setSourceImage(loadImage("../assets/graph.png"));
		graph.calculateTargetImage();
	}

	public void draw() {
		background(255);
		graph.draw();
	}
}
