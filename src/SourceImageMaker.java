import processing.core.PApplet;

public class SourceImageMaker extends PApplet {

	private static final int HORIZONTAL_SPACING = 10;
	private static final int VERTICAL_SPACING = 10;

	public void setup() {
		size(600, 600);

		background(255);

		this.strokeWeight(2);

		for (int x = 0; x < width; x += HORIZONTAL_SPACING) {
			line(x, 0, x, height);
		}

		for (int y = 0; y < height; y += VERTICAL_SPACING) {
			line(0, y, width, y);
		}

		this.save("graph.png");
	}
}
