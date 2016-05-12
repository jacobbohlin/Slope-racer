package clientGUI;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Level {
	protected Group g;
	protected double width, height, widthMargin, heightMargin;
	protected float ratio;
	
	public Level(Group g, double width, double height, double widthMargin, double heightMargin, float ratio) {
		this.g = g;
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		this.widthMargin = widthMargin;
		this.heightMargin = heightMargin;
		buildLevel();
	}
	
	protected abstract void buildLevel();
	
}
