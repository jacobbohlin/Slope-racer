package test;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Val {
	public static final int RATIO = 30;
	public static World world;
	
	public static void createWorld() {
		world = new World(new Vec2(0.0f, 10.0f), true);	
	}
}
