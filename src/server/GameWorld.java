package server;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class GameWorld {
	
	private static float[][] playerData;
	protected static World world;
	private Vec2 gravity;
	private boolean allowSleepingObjects;
	private static HashMap<InetAddress, Player> players;
	private HashMap<InetAddress, MouseBall> mouseBalls;
	private HashMap<InetAddress, Skier> skiers;
	private Contact contact;
	private Body boostPadBody;
	
	public GameWorld(HashMap<InetAddress, Player> players){
		this.players = players;
		mouseBalls = new HashMap<InetAddress, MouseBall>();
		skiers = new HashMap<InetAddress, Skier>();
		playerData = new float[players.size()][3];
		gravity = new Vec2(0f, 0f);
		allowSleepingObjects = true;
		world = new World(gravity);
		world.setAllowSleep(allowSleepingObjects);
		createMouseBalls();
//		createSkiers();
	}

	/**
	 * Creates a mouseBall for each player to play with
	 */
	private void createMouseBalls(){
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			Vec2 position = new Vec2(3, 3);
			InetAddress addr = p.getAddress();
			mouseBalls.put(addr, new MouseBall(position));
		}
	}
	/**
	 * Creates the level for the mouseBalls to bounce on
	 * @return 
	 */
	
	
	
	/**
	 * Takes a step in time and fills the playerData-matrix with new data
	 */
	public void step(){
		world.step(1/60f, 10, 10);			
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			MouseBall b = mouseBalls.get(e.getValue().getAddress());
			int ratio = 20;
			playerData[p.getPlayerNbr()][0] = b.getPositionX();
			playerData[p.getPlayerNbr()][1] = b.getPositionY();
			playerData[p.getPlayerNbr()][2] = b.getRadius();
			
			float midX = 16.8f;
			float midY = 10.5f;
			float xPos = b.getBody().getPosition().x;
			float yPos = b.getBody().getPosition().y;
			boolean xBoost = xPos > (midX - 1) && xPos < (midX + 1);
			boolean yBoost = yPos > (midY - 1) && yPos < (midY + 1);
			
			if(xPos < 0 || xPos > midX*2 || yPos < 0 || yPos > yPos*2){
				b.getBody().setLinearDamping(500);
			}
			
			if(xBoost && yBoost){
				ratio = 3;
			}
			
			//Calculate and apply force to body depending on mouse position relative to body position
			float deltaX = p.getMouseX() - 50;
			float deltaY = p.getMouseY() - 50;
//			System.out.println(p.getMouseX());
			Vec2 impulse = new Vec2(deltaX/ratio, deltaY/ratio);
			b.getBody().applyForceToCenter(impulse);
			
			
			
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
