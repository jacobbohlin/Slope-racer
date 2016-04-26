package test;

//import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Body;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.Node;



public class Actor implements EventDispatcher {
	protected Body body;
	protected Node sprite;
	
	public Actor (Body body, Node sprite) {
		this.body = body;
		this.sprite = sprite;
		sprite.setUserData(body);
		
		updatePos();
	}
	
	public void update() {
//		if(body.m_type != BodyType.STATIC) {
//			updatePos();
//		}
		updatePos();
		updateChildren();
	}
	
	protected void updateChildren() {
		//Only for subclasses.
	}

	private void updatePos() {
		sprite.setLayoutX(body.getPosition().x * Val.RATIO);
		sprite.setLayoutY(body.getPosition().y * Val.RATIO);
		sprite.setRotate(body.getAngle() * Math.PI / 180);
	}
	
	public void destroy() {
		//TODO
	}
	
	public Node getSprite() {
		return sprite;
	}
	
	@Override
	public Event dispatchEvent(Event arg0, EventDispatchChain arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
