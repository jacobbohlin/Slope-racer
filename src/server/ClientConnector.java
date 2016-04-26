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
				if (willSendUpdate) {
					send();
				}
				socket.receive(dp);
				String input = new String(dp.getData(), 0, dp.getLength());
				if (input.startsWith("update;")) {
					update(input.substring(8));
				} else if (input.startsWith("connect;")) {
					connect(input.substring(8));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connect(String nickName) {
		players.put(dp.getAddress(), new Player(nickName, dp.getAddress(), dp.getPort()));
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
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream);
		oo.writeObject(GameWorld.playerData);
		byte[] buf = bStream.toByteArray();
		dp.setData(buf);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			dp.setAddress(e.getKey());
			dp.setPort(e.getValue().getPort());
			socket.send(dp);
		}
		willSendUpdate = false;
	}

	
	private void sendAck(Player p){
		String message = "ACK;" + p.getPlayerNbr();
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
