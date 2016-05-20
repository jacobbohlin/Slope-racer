package clientGUI;



import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class StandardLevel extends Level {
	private Image[] boostImages;
	private int whichImage = 0;
	private Rectangle boostPad;
	
	public StandardLevel(Group g, double width, double height, double widthMargin, double heightMargin, float ratio) {
		super(g, width, height, widthMargin, heightMargin, ratio);
	}
	
	protected void buildLevel() {
		boostImages = new Image[4];
		boostImages[0] = new Image("file:BoostPad0.png");
		boostImages[1] = new Image("file:BoostPad1.png");
		boostImages[2] = new Image("file:BoostPad2.png");
		boostImages[3] = new Image("file:BoostPad3.png");
		boostPad = new Rectangle(width/2 - ratio + widthMargin, height/2 - ratio + heightMargin, ratio * 2, ratio * 2);
		Rectangle borders = new Rectangle(widthMargin, heightMargin, width, height);
		borders.setFill(new ImagePattern(new Image("file:BarbedWire.png")));
		boostPad.setFill(new ImagePattern(boostImages[whichImage++]));
		borders.toBack();
		g.getChildren().addAll(borders, boostPad);
	}
	
	public void update() {
		if(whichImage == 4) {
			whichImage = 0;
		}
		boostPad.setFill(new ImagePattern(boostImages[whichImage++]));
	}

}
