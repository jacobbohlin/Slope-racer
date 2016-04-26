package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reciever extends Thread {
	private HashMap<InetAddress, Player> players;
	private DatagramSocket socket;

	public Reciever(HashMap<InetAddress, Player> players) {
		this.players = players;
		try {
			socket = new DatagramSocket(8888);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run(){
		DatagramPacket dp = new DatagramPacket(new byte[1000], 1000);
		try {
			socket.receive(dp);
			receive(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receive(DatagramPacket dp) throws IOException{
		InetAddress adress = dp.getAddress();
		String input = new String(dp.getData(), 0, dp.getLength());
		String[] inputArray = input.split(";");
		players.get(adress).setMousePos(Integer.parseInt(inputArray[0]),Integer.parseInt(inputArray[1]));
	}
	
}
