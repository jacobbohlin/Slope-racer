package server;

import java.net.InetAddress;

public class Player {
	private int port;
	private String name;
	private InetAddress ip;
	private int mouseX, mouseY, mouseClick;
	private final int playerNbr;
	private static int nextPlayerNbr = 0;
	private boolean isDead;
	private int score;
	
	/**
	 * Constructs a player.
	 * @param name, Name of this player.
	 * @param port, UDP port of this player.
	 */
	public Player(String name, InetAddress ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		mouseX = 50;
		mouseY = 50;
		mouseClick = -1;
		playerNbr = nextPlayerNbr++;
		isDead = false;
		score = 0;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return score;
	}
	/**
	 * 
	 * @return true if player is dead
	 */
	public boolean isDead(){return isDead;}
	
	public void kill(){
		isDead = true;
	}
	public void revive(){
		isDead = false;
	}
	
	/**
	 * Sets the mouse position of this player.
	 * @param mouseClick 
	 * @param x, mouse x position in percent.
	 * @param y, mouse y position in percent.
	 */
	public void setMousePos(int x, int y, int mouseClick) {
		mouseX = x;
		mouseY = y;
		this.mouseClick = mouseClick;
	}
	
	/**
	 * @return mouse x position of this player.
	 */
	public int getMouseX() {return mouseX;}
	
	/**
	 * @return mouse y position of this player.
	 */
	public int getMouseY() {return mouseY;}
	
	/**
	 * 
	 * @return mouse button currently pressen, -1 for nothing, 0 for leftclick, 1 for rightclick
	 */
	public int getMouseClick() {return mouseClick;}
	
	/**
	 * @return the UDP-Port of this player.
	 */
	public int getPort() {return port;}
	
	/**
	 * 
	 * @return the playerNbr of this playa~.
	 */
	public int getPlayerNbr() {return playerNbr;}

	public InetAddress getAddress() {return ip;}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getName(){
		return name;
	}

	
}
