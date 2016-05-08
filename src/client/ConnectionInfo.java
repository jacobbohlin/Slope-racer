package client;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionInfo {
	private static InetAddress ip;
	private static int port;
	private static DatagramSocket socket;
	private static String name;
	private static int id = -1;
	private static float[][] playerData;
	private static boolean firstPacketReceived = false;

	
	public static InetAddress getIp() { 
		return ip;
	}
	
	public static void setIp(InetAddress ip) {
		ConnectionInfo.ip = ip;
	}
	
	public static int getPort() {
		return port;
	}
	
	public static void setPort(int port) {
		ConnectionInfo.port = port;
	}
	
	public static DatagramSocket getSocket() {
		return socket;
	}
	
	public static void setSocket(DatagramSocket socket) {
		ConnectionInfo.socket = socket;
	}
	
	public static String getName() {
		return name;
	}
	
	public static void setName(String name) {
		ConnectionInfo.name = name;
	}
	
	public static int getId() {
		return id;
	}
	
	public static void setId(int id) {
		ConnectionInfo.id = id;
	}
	
	public static float[][] getPlayerData() {
		return playerData;
	}
	
	public static void setPlayerData(float[][] playerData) {
		ConnectionInfo.playerData = playerData;
	}
	
	public static boolean isFirstPacketReceived() {
		return firstPacketReceived;
	}
	
	public static void setFirstPacketReceived() {
		firstPacketReceived = true;
	}
}
