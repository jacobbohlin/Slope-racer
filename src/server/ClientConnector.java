package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientConnector extends Thread {
	public static HashMap<InetAddress, Player> players = new HashMap<InetAddress, Player>();
	private DatagramSocket socket;
	public boolean willSendUpdate;
	private DatagramPacket dp;

	public ClientConnector() {
		willSendUpdate = false;
		dp = new DatagramPacket(new byte[1000], 1000);
		try {
			socket = new DatagramSocket(8888);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		
		while (true) {
			try {
				if(willSendUpdate){
					send();
				}
				socket.receive(dp);
				
				update(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void update(DatagramPacket dp) throws IOException {
		InetAddress adress = dp.getAddress();
		String input = new String(dp.getData(), 0, dp.getLength());
		String[] inputArray = input.split(";");
		players.get(adress).setMousePos(Integer.parseInt(inputArray[0]), Integer.parseInt(inputArray[1]));
	}
	
	private void send() throws IOException{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(GameWorld.playerData);
		byte [] buf = bStream.toByteArray();
		dp.setData(buf);
		for(Entry<InetAddress, Player> e : players.entrySet()){
			dp.setAddress(e.getKey());
			dp.setPort(e.getValue().getPort());
			socket.send(dp);
		}
		willSendUpdate = false;
	}

}
