import processing.core.PApplet;

public class Main extends PApplet {
	ComplexGraph graph;

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
			graph.addPointToGraph(mouseX, mouseY, color(255, 0, 0));
			graph.addPointToGraph(mouseX + 1, mouseY, color(255, 0, 0));
			graph.addPointToGraph(mouseX - 1, mouseY, color(255, 0, 0));
			graph.addPointToGraph(mouseX, mouseY + 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX, mouseY - 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX + 1, mouseY + 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX - 1, mouseY + 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX + 1, mouseY + 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX + 1, mouseY - 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX + 1, mouseY - 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX - 1, mouseY - 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX - 1, mouseY + 1, color(255, 0, 0));
			graph.addPointToGraph(mouseX - 1, mouseY - 1, color(255, 0, 0));
		}
	}
	
	public void updateGraph(){
		graph.calculateTargetImage();
	}
}
