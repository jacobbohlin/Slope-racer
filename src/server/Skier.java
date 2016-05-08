package server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Skier {

	private Body body;

	private Vec2 upperBodyPosition;
	private Vec2 midBodyPosition;
	private Vec2 lowerBodyPosition;

	public Skier(Vec2 position) {
		upperBodyPosition = position;
		// midBodyPosition = position - lite;
		// lowerBodyPosition = position - lite mer;

		// Create a body definition for this mouseBall
		Body upperBody = createUpperBody();

		Body midBody = createMidBody();

		Body lowerBody = createLowerBody();



		// FixtureDef fd = new FixtureDef();
		// fd.shape = cs;
		// fd.density = 0.6f;
		// fd.friction = 0.3f;
		// fd.restitution = 0.8f;

		// Create joint definition
		RevoluteJointDef upperJoint = new RevoluteJointDef();
		upperJoint.bodyA = upperBody;
		RevoluteJointDef lowerJoint = new RevoluteJointDef();


	}

	private Body createUpperBody() {

		// Create shapes
		PolygonShape upperBodyShape = new PolygonShape();
		upperBodyShape.setAsBox(1f, 0.25f);
		
		CircleShape headShape = new CircleShape();
		headShape.m_radius = 0.08f;
		headShape.m_p.set(new Vec2(0, 0.58f));

		// Create fixtures

		// Head
		FixtureDef headFixture = new FixtureDef();
		headFixture.shape = headShape;
		headFixture.density = 1.5f;
		headFixture.friction = 1f;
		headFixture.restitution = 5f;
		// Upper Body
		FixtureDef upperBodyFixture = new FixtureDef();
		upperBodyFixture.shape = upperBodyShape;
		upperBodyFixture.density = 1f;
		upperBodyFixture.friction = 1f;
		upperBodyFixture.restitution = 0.3f;

		// Create body Definition and add body to world
		BodyDef topDef = new BodyDef();
		topDef.type = BodyType.DYNAMIC;
		topDef.position.set(upperBodyPosition);
		Body upperBody = GameWorld.world.createBody(topDef);

		// Add our fixtures to the body
		upperBody.createFixture(headFixture);
		upperBody.createFixture(upperBodyFixture);

		return upperBody;

	}

	private Body createMidBody() {
		
		//Create shape
		PolygonShape thighShape = new PolygonShape();
		thighShape.setAsBox(0.5f, 0.25f);
		
		//Create Fixture
		FixtureDef midFixture = new FixtureDef();
		midFixture.shape = thighShape;
		midFixture.density = 1f;
		midFixture.friction = 1f;
		midFixture.restitution = 0.3f;
		
		// Create body Definition and add body to world
		BodyDef midDef = new BodyDef();
		midDef.type = BodyType.DYNAMIC;
		midDef.position.set(midBodyPosition);
		Body midBody = GameWorld.world.createBody(midDef);
		
		// Add our fixture to the body
		midBody.createFixture(midFixture);


		return midBody;
	}

	private Body createLowerBody() {
		
		//Create shape
		PolygonShape calfShape = new PolygonShape();
		calfShape.setAsBox(0.5f, 0.2f);
		
		PolygonShape skiShape = new PolygonShape();
		skiShape.setAsBox(0.02f, 2f, new Vec2(0.2f, 0.26f), 0);
		
		//Create Fixture
		FixtureDef calfFixture = new FixtureDef();
		calfFixture.shape = calfShape;
		calfFixture.density = 1f;
		
		FixtureDef skiFixture = new FixtureDef();
		skiFixture.shape = skiShape;
		skiFixture.density = 0.3f;
		skiFixture.friction = 0.1f;
		skiFixture.restitution = 0.3f;
		
		// Create body Definition and add body to world
		BodyDef lowerDef = new BodyDef();
		lowerDef.type = BodyType.DYNAMIC;
		lowerDef.position.set(lowerBodyPosition);
		Body lowerBody = GameWorld.world.createBody(lowerDef);
		
		// Add our fixture to the body
		lowerBody.createFixture(calfFixture);
		lowerBody.createFixture(skiFixture);


		return lowerBody;
	}

	public float getPositionX() {
		return body.getPosition().x;
	}

	public float getPositionY() {
		return body.getPosition().y;
	}
}
