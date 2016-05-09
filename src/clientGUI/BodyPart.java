package clientGUI;

import javafx.scene.paint.Color;

public class BodyPart {
	private double xPos, yPos, angle, width, height;
	private Color color; //Ska ändras till image.
	
	public BodyPart(double width, double height) {
		this.width = width;
		this.height = height;
		xPos = 0;
		yPos = 0;
		angle = 0;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}
