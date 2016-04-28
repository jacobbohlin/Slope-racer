package server;

import java.net.InetAddress;
import java.util.HashMap;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class GameWorld {
	
	public static float[][] playerData = new float[][]{
		{1f, 2f, 3f, 4f},
		{2f, 4f, 6f, 8f}, 
		{4f, 8f, 12f, 16f}
	};
//	private final World world;
	private Vec2 gravity;
	private boolean allowSleepingObjects;
	private static HashMap<InetAddress, Player> players;
	
//	public GameWorld(){
//		gravity = new Vec2(0f, -9.8f);
//		allowSleepingObjects = true;
//		world = new World(gravity, allowSleepingObjects);
//	}

}
