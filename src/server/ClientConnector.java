package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientConnector extends Thread {
	private HashMap<InetAddress, Player> players = new HashMap<InetAddress, Player>();
	private DatagramSocket socket;
	public boolean willSendUpdate;
	private DatagramPacket dp;

	public ClientConnector() {
		willSendUpdate = false;
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
				dp = new DatagramPacket(new byte[1000], 1000);
				socket.receive(dp);
				String input = new String(dp.getData(), 0, dp.getLength());
				System.out.println("Received packet: " + input);
				if (input.startsWith("update;")) {
					update(input.substring(7));
				} else if (input.startsWith("connect;")) {
					System.out.println("A client is trying to connect.");
					connect(input.substring(8));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connect(String nickName) {
		if (players.containsKey(dp.getAddress())) {
			System.out.println("That IP has already connected");
			players.get(dp.getAddress()).setPort(dp.getPort());
		} else {
			players.put(dp.getAddress(), new Player(nickName, dp.getAddress(), dp.getPort()));
			System.out.println("A new player has connected");
		}
		Player p = players.get(dp.getAddress());
		sendAck(p);
	}

	private void update(String input) throws IOException {

		if(!input.isEmpty()){
			String[] inputArray = input.split(";");
			int mouseX = (int) Double.parseDouble(inputArray[0]);			
			int mouseY = (int) Double.parseDouble(inputArray[1]);
			players.get(dp.getAddress()).setMousePos(mouseX, mouseY);
		}
	}

	public void send() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(GameWorld.getPlayerData());
		byte[] buf = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			packet.setAddress(e.getKey());
			packet.setPort(e.getValue().getPort());
//			System.out.println("sending update to: " + packet.getAddress() + " " + packet.getPort() + " X: " 
//			+ GameWorld.getPlayerData()[e.getValue().getPlayerNbr()][0] + " Y: " + GameWorld.getPlayerData()[e.getValue().getPlayerNbr()][1]);
			socket.send(packet);
		}
	}

	private void sendAck(Player p) {
		String message = "ACK;" + p.getPlayerNbr();
		byte[] buf = message.getBytes();
		dp = new DatagramPacket(buf, buf.length);
		dp.setAddress(p.getAddress());
		dp.setPort(p.getPort());
		System.out.println("Sending ack to: " + dp.getAddress() + " " + dp.getPort() + " Message: " + new String(dp.getData(), 0, dp.getLength()));
		try {
			socket.send(dp);
		} catch (IOException e) {
			System.out.println("Something went wrong while sending ACK");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return HashMap containing all the Players currently connected
	 */
	public synchronized HashMap<InetAddress, Player> getPlayers() {
		return players;
	}

}
