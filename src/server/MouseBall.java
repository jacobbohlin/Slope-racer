package server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class MouseBall {

	private Body body;

	private Vec2 position;

	public MouseBall(Vec2 position) {
		this.position = position;

		// Create a body definition for this mouseBall
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.linearDamping = 0.5f;
		bd.position.set(position);

		// Create a shape for this mouseBall
		CircleShape cs = new CircleShape();
		cs.m_radius = 0.5f;

		// Create a fixture for this mouseBall
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0.6f;
		fd.friction = 0.3f;
		fd.restitution = 0.8f;

		body = GameWorld.world.createBody(bd);
		body.createFixture(fd);
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
}
