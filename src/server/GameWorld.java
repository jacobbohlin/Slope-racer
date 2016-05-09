package server;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class GameWorld {
	
	private static float[][] playerData;
	protected static World world;
	private Vec2 gravity;
	private boolean allowSleepingObjects;
	private static HashMap<InetAddress, Player> players;
	private HashMap<InetAddress, MouseBall> mouseBalls;
	private HashMap<InetAddress, Skier> skiers;
	
	public GameWorld(HashMap<InetAddress, Player> players){
		this.players = players;
		mouseBalls = new HashMap<InetAddress, MouseBall>();
		skiers = new HashMap<InetAddress, Skier>();
		playerData = new float[players.size()][12];
		gravity = new Vec2(0f, 0.4f);
		allowSleepingObjects = true;
		world = new World(gravity);
		world.setAllowSleep(allowSleepingObjects);
//		createMouseBalls();
		createSkiers();
		createLevel();
	}
	
	private void createSkiers() {
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			Vec2 position = new Vec2(3, 3);
			InetAddress addr = p.getAddress();
			skiers.put(addr, new Skier(position));
		}		
	}

	/**
	 * Creates a mouseBall for each player to play with
	 */
	private void createMouseBalls(){
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			Vec2 position = new Vec2(10, 1);
			InetAddress addr = p.getAddress();
			mouseBalls.put(addr, new MouseBall(position));
		}
	}
	/**
	 * Creates the level for the mouseBalls to bounce on
	 */
	private void createLevel(){
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(300f, 1f);
		
		FixtureDef groundFixture = new FixtureDef();
		groundFixture.shape = groundShape;
		groundFixture.restitution = 0.1f;
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.STATIC;
		groundBodyDef.position.set(new Vec2(0f, 15f));
		Body groundBody = world.createBody(groundBodyDef);
		groundBody.createFixture(groundFixture);
		
		
		
	}
	/**
	 * Takes a step in time and fills the playerData-matrix with new data
	 */
	public void step(){
		world.step(1/60f, 10, 10);
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			Skier s = skiers.get(p.getAddress());
			int i = 0;
			//Update playerData with correct body positions
			for(float f: s.getPosition()){
				playerData[p.getPlayerNbr()][i] = f;
				i++;
			}
			
			//Calculate and apply force to body depending on mouse position relative to body position
//			float deltaX = p.getMouseX() - 50;
//			float deltaY = p.getMouseY() - 50;
//			Vec2 impulse = new Vec2(deltaX/20, deltaY/20);
//			b.getBody().applyForceToCenter(impulse);
		}
	}
	
	/**
	 * 
	 * @return matrix containing current positions of all bodies
	 */
	public static synchronized float[][] getPlayerData(){
		return playerData;
	}

}
