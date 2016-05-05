package server;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class GameWorld {
	
	private static float[][] playerData;
	protected static World world;
	private Vec2 gravity;
	private boolean allowSleepingObjects;
	private static HashMap<InetAddress, Player> players;
	private HashMap<InetAddress, MouseBall> mouseBalls;
	
	public GameWorld(HashMap<InetAddress, Player> players){
		this.players = players;
		mouseBalls = new HashMap<InetAddress, MouseBall>();
		playerData = new float[players.size()][2];
		gravity = new Vec2(0f, 0.3f);
		allowSleepingObjects = true;
		world = new World(gravity);
		world.setAllowSleep(allowSleepingObjects);
		createMouseBalls();
	}
	
	/**
	 * Creates a mouseBall for each player to play with
	 */
	private void createMouseBalls(){
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			Vec2 position = new Vec2(5 + p.getPlayerNbr()*10, 0);
			InetAddress addr = p.getAddress();
			mouseBalls.put(addr, new MouseBall(position));
		}
	}
	/**
	 * Creates the level for the mouseBalls to bounce on
	 */
	private void createLevel(){
		
	}
	/**
	 * Takes a step in time and fills the playerData-matrix with new data
	 */
	public void step(){
		world.step(1/30f, 10, 10);
		for(Entry<InetAddress, Player> e : players.entrySet()){
			Player p = e.getValue();
			MouseBall b = mouseBalls.get(p.getAddress());
			
			//Update playerData with correct body positions
			playerData[p.getPlayerNbr()][0] = b.getPositionX();
			playerData[p.getPlayerNbr()][1] = b.getPositionY();
			
			//Calculate and apply force to body depending on mouse position relative to body position
			float deltaX = p.getMouseX() - playerData[p.getPlayerNbr()][0];
			float deltaY = p.getMouseY() - playerData[p.getPlayerNbr()][1];
			Vec2 impulse = new Vec2(deltaX/5, deltaY/5);
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
