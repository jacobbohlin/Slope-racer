package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientOld.ConnectionException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ConnectService extends Service<String> {
	private final int PORT = ConnectionInfo.PORT;
	private final InetAddress IP = ConnectionInfo.IP;
	private final DatagramSocket SOCKET = ConnectionInfo.SOCKET;
	private final String NAME = ConnectionInfo.NAME;

	@Override
	protected Task<String> createTask() {
		Task t = new Task<String>() {
			@Override
			protected String call() throws Exception {
				byte[] handshake = ("connect;" + NAME).getBytes();
				DatagramPacket connect = new DatagramPacket(handshake, handshake.length);
				connect.setAddress(IP);
				connect.setPort(PORT);
				SOCKET.send(connect);
				DatagramPacket response = new DatagramPacket(new byte[1000], 1000);
				SOCKET.setSoTimeout(1000);
				SOCKET.receive(response);
				if(response.getPort() == -1) {
					System.out.println("Trying to connect...");
					return "Trying to connect...";			
				} 
				System.out.println("Connected " + new String(response.getData(), 0, response.getLength()));
				return "Connected";
			}
		};
		return t;
	}
}
