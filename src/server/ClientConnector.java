package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientConnector extends Thread {
	public static HashMap<InetAddress, Player> players = new HashMap<InetAddress, Player>();
	private DatagramSocket socket;
	public boolean willSendUpdate;
	private DatagramPacket dp;


	public ClientConnector() {
		willSendUpdate = true;
		dp = new DatagramPacket(new byte[1000], 1000);
		try {
			socket = new DatagramSocket(8888);
		} catch (SocketException e) {
			System.out.println("Could not create socket");
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				if (willSendUpdate && !players.isEmpty()) {
					send();
				}
				dp = new DatagramPacket(new byte[1000], 1000);
				socket.receive(dp);
				String input = new String(dp.getData(), 0, dp.getLength());
				System.out.println("Received packet: " + input);
				if (input.startsWith("update;")) {
					update(input.substring(8));
				} else if (input.startsWith("connect;")) {
					System.out.println("A new client is trying to connect.");
					connect(input.substring(8));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connect(String nickName) {
		if(players.containsKey(dp.getAddress())){
			System.out.println("That IP has already connected");
		} else {
		players.put(dp.getAddress(), new Player(nickName, dp.getAddress(), dp.getPort()));
		System.out.println("A new player has connected");
		}
		Player p = players.get(dp.getAddress());
		sendAck(p);
	}

	private void update(String input) throws IOException {

		String[] inputArray = input.split(";");
		int mouseX = Integer.parseInt(inputArray[0]);
		int mouseY = Integer.parseInt(inputArray[1]);
		players.get(dp.getAddress()).setMousePos(mouseX, mouseY);
	}

	private void send() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(GameWorld.playerData);
		byte[] buf = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			packet.setAddress(e.getKey());
			packet.setPort(e.getValue().getPort());
			socket.send(packet);
		}
		willSendUpdate = false;
	}

	
	private void sendAck(Player p){
		String message = "ACK;" + p.getPlayerNbr();
		System.out.println("Sending ack to: " + p.getAdress()+ " " + message + " " + p.getPort());
		dp = new DatagramPacket(new byte[1000], 1000);
		dp.setData(message.getBytes());
		dp.setAddress(p.getAdress());
		dp.setPort(p.getPort());
		try {
			socket.send(dp);
		} catch (IOException e) {
			System.out.println("Something went wrong while sending ACK");
			e.printStackTrace();
		}
	}
}
