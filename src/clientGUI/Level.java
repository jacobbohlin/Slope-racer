package clientGUI;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Level {
	protected Group g;
	protected double width, height;
	protected float ratio;
	
	public Level(Group g, double width, double height, float ratio) {
		this.g = g;
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		buildLevel();
	}
	
	protected abstract void buildLevel();
	
}
