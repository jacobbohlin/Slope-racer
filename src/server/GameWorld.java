package server;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TreeMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import javafx.scene.media.AudioClip;

public class GameWorld {

	private static float[][] playerData;
	private World world;
	private Vec2 gravity;
	private boolean allowSleepingObjects;
	private static HashMap<InetAddress, Player> players;
	private HashMap<InetAddress, Player> playersAlive;
	private HashMap<InetAddress, MouseBall> mouseBalls;
	private Contact contact;
	private Body boostPadBody;
	private Timer cooldownTimer;
	private boolean cooldown;
	private ClientConnector connector;

	public GameWorld(HashMap<InetAddress, Player> players, ClientConnector connector) {
		this.players = players;
		playersAlive = new HashMap<InetAddress, Player>();
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			playersAlive.put(e.getKey(), e.getValue());
		}
		cooldownTimer = new Timer();

		cooldown = false;
		mouseBalls = new HashMap<InetAddress, MouseBall>();
		playerData = new float[players.size()][3];
		gravity = new Vec2(0f, 0f);
		allowSleepingObjects = true;
		world = new World(gravity);
		world.setContactListener(new ContactListenerClass(connector)); // Dedicated
																		// listener
																		// for
																		// audio
																		// to
																		// play
																		// on
																		// collision
		world.setAllowSleep(allowSleepingObjects);
		createMouseBalls();
		this.connector = connector;
	}

	public World getWorld() {
		return world;
	}

	/**
	 * Creates a mouseBall for each player to play with
	 */
	private void createMouseBalls() {
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			Player p = e.getValue();
			Vec2 position = new Vec2((float) (3 + Math.random() * 26), (float) (3 + Math.random() * 12));
			InetAddress addr = p.getAddress();
			mouseBalls.put(addr, new MouseBall(position, world));
		}
	}
	/**
	 * Creates the level for the mouseBalls to bounce on
	 * 
	 * @return
	 */

	/**
	 * Takes a step in time and fills the playerData-matrix with new data
	 */
	public void step() {
		world.step(1 / 60f, 10, 10);
		if (playersAlive.size() == 1 && players.size() > 1) {
			for (Entry<InetAddress, Player> e : playersAlive.entrySet()) {
				e.getValue().setScore(e.getValue().getScore() + 1);
				playersAlive.remove(e.getKey());
			}
		}
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			if (playersAlive.containsKey(e.getKey()) || players.size() == 1) {
				Player p = e.getValue();
				MouseBall b = mouseBalls.get(e.getValue().getAddress());

				int ratio = 10;
				playerData[p.getPlayerNbr()][0] = b.getPositionX();
				playerData[p.getPlayerNbr()][1] = b.getPositionY();
				playerData[p.getPlayerNbr()][2] = b.getRadius();

				// Check if ball is expanding
				if (25 < b.getExpandCounter()) {
					b.setRadius(b.getRadius() + 0.04f);
					b.decrementExpandCounter();
					connector.sendSoundEffectCue("expball");
				} else if (0 < b.getExpandCounter()) {
					b.setRadius(b.getRadius() - 0.04f);
					b.decrementExpandCounter();
				} else if (b.getExpandCounter() == 0) {
					b.setRadius(0.5f);
					b.decrementExpandCounter();
				}

				// Check if ball is minimizing
				if (75 < b.getMinimizeCounter()) {
					b.setRadius(b.getRadius() - 0.016f);
					b.decrementMinimizeCounter();
					connector.sendSoundEffectCue("conball");
				} else if (25 < b.getMinimizeCounter()) {
					b.decrementMinimizeCounter();
				} else if (0 < b.getMinimizeCounter()) {
					b.setRadius(b.getRadius() + 0.016f);
					b.decrementMinimizeCounter();
				} else if (b.getExpandCounter() == 0) {
					b.setRadius(0.5f);
					b.decrementMinimizeCounter();
				}

				// Check if ball is on boost pad
				float midX = 16f;
				float midY = 9f;
				float xPos = b.getBody().getPosition().x;
				float yPos = b.getBody().getPosition().y;
				boolean xBoost = xPos > (midX - 1) && xPos < (midX + 1);
				boolean yBoost = yPos > (midY - 1) && yPos < (midY + 1);
				if (xBoost && yBoost) {
					ratio = 2;
					connector.sendSoundEffectCue("boostpd");

				}
				// Check if ball is out of bounds
				if (xPos < 0.5 + b.getRadius() || xPos > midX * 2 - (0.5 + b.getRadius()) || yPos < 0.5 + b.getRadius()
						|| yPos > midY * 2 - (0.5 + b.getRadius())) {
					b.getBody().m_type = BodyType.STATIC;
					b.kill();
					playersAlive.remove(e.getKey());
					connector.sendSoundEffectCue("bwdeath");
				}

				// Calculate and apply force to body depending on mouse position
				// relative to body position
				float deltaX = p.getMouseX() - 50;
				float deltaY = p.getMouseY() - 50;
				// System.out.println(p.getMouseX());
				Vec2 impulse = new Vec2(deltaX / ratio, deltaY / ratio);
				b.getBody().applyForceToCenter(impulse);
				if (!b.isCooldown()) {
					if (p.getMouseClick() == 0) {
						b.expand();
						b.cooldown(2000);
					} else if (p.getMouseClick() == 1) {
						b.minimize();
						b.cooldown(2000);
					}

				}
			}

		}
	}

	/**
	 * 
	 * @return matrix containing current positions of all bodies
	 */
	public synchronized float[][] getPlayerData() {
		return playerData;
	}
}
