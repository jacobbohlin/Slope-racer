package client;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionInfo {
	public static InetAddress IP;
	public static int PORT;
	public static DatagramSocket SOCKET;
	public static String NAME;
	
	public static void setIP(InetAddress ip) {
		IP = ip;
	}
	
	public static void setPort(int port) {
		PORT = port;
	}
	
	public static void setSocket(DatagramSocket socket) {
		SOCKET = socket;
	}
	
	public static void setName(String name) {
		NAME = name;
	}
}
