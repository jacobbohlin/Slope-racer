package server;

import java.net.InetAddress;

public class Player {
	private int port;
	private String name;
	private InetAddress ip;
	private int mouseX, mouseY;
	private final int playerNbr;
	private static int nextPlayerNbr = 0;
	
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
		playerNbr = nextPlayerNbr++;
	}
	
	/**
	 * Sets the mouse position of this player.
	 * @param x, mouse x position in percent.
	 * @param y, mouse y position in percent.
	 */
	public void setMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
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
}
