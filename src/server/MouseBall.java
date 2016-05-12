package server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class MouseBall {
	
	private int boostCooldown;
	private int expandCounter;
	private int minimizeCounter;

	private Body body;

	private Vec2 position;
	private float radius;

	public MouseBall(Vec2 position) {
		this.position = position;
		expandCounter = -1;
		minimizeCounter = -1;
		radius = 0.5f;

		// Create a body definition for this mouseBall
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.linearDamping = 0.5f;
		bd.position.set(position);

		// Create a shape for this mouseBall
		CircleShape cs = new CircleShape();
		cs.m_radius = radius;
		

		// Create a fixture for this mouseBall
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0.6f;
		fd.friction = 0.3f;
		fd.restitution = 0.8f;

		body = GameWorld.world.createBody(bd);
		body.createFixture(fd);
	}
	
	public void setRadius(float radius){
		Fixture f = body.getFixtureList();
		f.m_shape.setRadius(radius);
	}
	
	public Body getBody(){
		return body;
	}

	public float getPositionX() {
		return body.getPosition().x;
	}
	
	public float getPositionY(){
		return body.getPosition().y;
	}

	public float getRadius() {
		return body.getFixtureList().m_shape.m_radius;
	}

	public void expand() {
		expandCounter = 50;
		
	}
	public int getExpandCounter(){
		return expandCounter;
	}

	public void decrementExpandCounter() {
		expandCounter -= 1;
	}

	public void minimize() {
		minimizeCounter = 100;
	}
	
	public int getMinimizeCounter(){
		return minimizeCounter;
	}
	
	public void decrementMinimizeCounter(){
		minimizeCounter -= 1;
	}
	
	public void kill(){
		minimizeCounter = -1;
		expandCounter = -1;
	}
	
}
