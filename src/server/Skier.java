package server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class Skier {

	private Body body;

	private Vec2 upperBodyPosition;
	private Vec2 armsPosition;
	private Vec2 midBodyPosition;
	private Vec2 lowerBodyPosition;
	
	private Body upperBody;
	private Body armsBody;
	private Body midBody;
	private Body lowerBody;
	
	private WeldJoint waistJoint;
//	private RevoluteJoint waistJoint;
	private RevoluteJoint shoulderJoint;
	private RevoluteJoint kneeJoint;

	public Skier(Vec2 position) {
		upperBodyPosition = position.add(new Vec2(0f, -0.5f));
		armsPosition = position.add(new Vec2(0f, -0.35f));
		midBodyPosition = position.add(new Vec2(0f, 0.25f));
		lowerBodyPosition = position.add(new Vec2(0f, 0.75f));

		upperBody = createUpperBody();
		
//		armsBody = createArms();
//
		midBody = createMidBody();
//
//		lowerBody = createLowerBody();
		
//		lowerBody.setAngularVelocity((float)Math.PI*2);

		waistJoint = createWaistJoint();
//		
//		shoulderJoint = createShoulderJoint();
//		
//		kneeJoint = createKneeJoint();
		
//		upperBody.applyForceToCenter(new Vec2(10f, 0f));
		
	}

	

//	private RevoluteJoint createWaistJoint() {
//		RevoluteJointDef waistJointDef = new RevoluteJointDef();
//		waistJointDef.bodyA = upperBody;
//		waistJointDef.bodyB = midBody;
//		waistJointDef.collideConnected = false;
//		waistJointDef.localAnchorA.set(new Vec2(0f, 0.5f));
//		waistJointDef.localAnchorB.set(new Vec2(0f, -0.25f));
//		
////		waistJointDef.referenceAngle = (float)Math.PI;
//		RevoluteJoint waistJoint = (RevoluteJoint)GameWorld.world.createJoint(waistJointDef);
////		waistJoint.enableMotor(false);
////		waistJoint.enableLimit(false);
////		waistJoint.setLimits((float)Math.PI/2, (float)Math.PI);
////		waistJoint.setMotorSpeed(-0.5f);	
//		Vec2 anchorA = new Vec2(); 
//		waistJoint.getAnchorA(anchorA);
//		Vec2 anchorB = new Vec2();
//		waistJoint.getAnchorB(anchorB);
//		System.out.println(anchorA.x + " " + anchorA.y);
//		System.out.println(anchorB.x + " " + anchorB.y);
//
//		return waistJoint;
//	}
	
	//TEST
	private WeldJoint createWaistJoint() {
		WeldJointDef waistJointDef = new WeldJointDef();
		Vec2 anchor = upperBody.getWorldCenter();
		waistJointDef.initialize(upperBody, midBody, anchor);
		waistJointDef.collideConnected = false;
//		waistJointDef.referenceAngle = (float)Math.PI;
		WeldJoint waistJoint = (WeldJoint)GameWorld.world.createJoint(waistJointDef);
//		waistJoint.enableMotor(false);
//		waistJoint.enableLimit(false);
//		waistJoint.setLimits((float)Math.PI/2, (float)Math.PI);
//		waistJoint.setMotorSpeed(-0.5f);	
		Vec2 anchorA = new Vec2(); 
		waistJoint.getAnchorA(anchorA);
		Vec2 anchorB = new Vec2();
		waistJoint.getAnchorB(anchorB);
		System.out.println(anchorA.x + " " + anchorA.y);
		System.out.println(anchorB.x + " " + anchorB.y);
		
		return waistJoint;
	}
	
	private RevoluteJoint createShoulderJoint(){
		RevoluteJointDef shoulderJointDef = new RevoluteJointDef();
		shoulderJointDef.bodyA = upperBody;
		shoulderJointDef.bodyB = armsBody;
		shoulderJointDef.collideConnected = false;
		shoulderJointDef.localAnchorA.set(new Vec2(0f, -0.45f));
		shoulderJointDef.localAnchorB.set(new Vec2(0f, -0.6f));
		
		RevoluteJoint shoulderJoint = (RevoluteJoint)GameWorld.world.createJoint(shoulderJointDef);
		
		return shoulderJoint;
	}
	
	private RevoluteJoint createKneeJoint() {
		RevoluteJointDef kneeJointDef = new RevoluteJointDef();
		kneeJointDef.bodyA = midBody;
		kneeJointDef.bodyB = lowerBody;
		kneeJointDef.collideConnected = false;
		kneeJointDef.localAnchorA.set(new Vec2(0f, 0.25f));
		kneeJointDef.localAnchorB.set(new Vec2(0f, -0.25f));
		
		RevoluteJoint kneeJoint = (RevoluteJoint)GameWorld.world.createJoint(kneeJointDef);
		
		return kneeJoint;
	}

	private Body createUpperBody() {

		// Create shapes
		PolygonShape upperBodyShape = new PolygonShape();
//		upperBodyShape.setAsBox(0.25f, 1f);
		upperBodyShape.setAsBox(2.5f, 10f);

		CircleShape headShape = new CircleShape();
		headShape.m_radius = 0.08f;
		headShape.m_p.set(new Vec2(0, -0.58f));

		// Create fixtures

		// Head
		FixtureDef headFixtureDef = new FixtureDef();
		headFixtureDef.shape = headShape;
		headFixtureDef.density = 1.5f;
		headFixtureDef.friction = 1f;
		headFixtureDef.restitution = 0.6f;
		headFixtureDef.filter.groupIndex = -1;
	
		// Upper Body
		FixtureDef upperBodyFixture = new FixtureDef();
		upperBodyFixture.shape = upperBodyShape;
		upperBodyFixture.density = 1f;
		upperBodyFixture.friction = 1f;
		upperBodyFixture.restitution = 0.1f;
		upperBodyFixture.filter.groupIndex = -1;

		// Create body Definition and add body to world
		BodyDef topDef = new BodyDef();
		topDef.type = BodyType.DYNAMIC;
		topDef.position.set(upperBodyPosition);
		Body upperBody = GameWorld.world.createBody(topDef);
		

		// Add our fixtures to the body
		upperBody.createFixture(headFixtureDef);
		upperBody.createFixture(upperBodyFixture);

		return upperBody;

	}
	
	private Body createArms(){
		//Create shape
		PolygonShape armsShape = new PolygonShape();
//		armsShape.setAsBox(0.15f, 1.2f);
		armsShape.setAsBox(2f, 15f);
		
		//Create Fixture
		FixtureDef armsFixture = new FixtureDef();
		armsFixture.shape = armsShape;
		armsFixture.density = 2f;
		armsFixture.friction = 1f;
		armsFixture.filter.groupIndex = -1;
		
		//Create body definition and add body to world
		BodyDef armsDef = new BodyDef();
		armsDef.type = BodyType.DYNAMIC;
		armsDef.position.set(armsPosition);
		Body armsBody = GameWorld.world.createBody(armsDef);
		
		//Add our fixture to the body
		armsBody.createFixture(armsFixture);
		
		return armsBody;
	}

	private Body createMidBody() {

		// Create shape
		PolygonShape thighShape = new PolygonShape();
//		thighShape.setAsBox(0.25f, 0.5f);
		thighShape.setAsBox(2f, 8f);

		// Create Fixture
		FixtureDef midFixture = new FixtureDef();
		midFixture.shape = thighShape;
		midFixture.density = 1f;
		midFixture.friction = 1f;
		midFixture.restitution = 0.1f;
		midFixture.filter.groupIndex = -1;

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

		// Create shape
		PolygonShape calfShape = new PolygonShape();
//		calfShape.setAsBox(0.2f, 0.5f);
		calfShape.setAsBox(0.2f, 0.5f);

		PolygonShape skiShape = new PolygonShape();
		skiShape.setAsBox(2f, 0.02f, new Vec2(0.26f, 0.2f), 0);

		// Create Fixture
		FixtureDef calfFixture = new FixtureDef();
		calfFixture.shape = calfShape;
		calfFixture.density = 1f;
		calfFixture.filter.groupIndex = -1;

		FixtureDef skiFixture = new FixtureDef();
		skiFixture.shape = skiShape;
		skiFixture.density = 0.3f;
		skiFixture.friction = 0.1f;
		skiFixture.restitution = 0.3f;
		skiFixture.filter.groupIndex = -1;

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

	/**
	 * Get the position and angle for each body. Sorted by x, y and angle.
	 * 
	 * @return float array
	 */
	public float[] getPosition() {
		float[] result = new float[12];
		result[0] = upperBody.getPosition().x;
		result[1] = upperBody.getPosition().y;
		result[2] = upperBody.getAngle();
//		result[3] = armsBody.getPosition().x;
//		result[4] = armsBody.getPosition().y;
//		result[5] = armsBody.getAngle();
		result[6] = midBody.getPosition().x;
		result[7] = midBody.getPosition().y;
		result[8] = midBody.getAngle();
//		result[9] = lowerBody.getPosition().x;
//		result[10] = lowerBody.getPosition().y;
//		result[11] = lowerBody.getAngle();
		return result;
	}
}
