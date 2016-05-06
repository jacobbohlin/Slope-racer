package client;

import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ConnectService extends Service<Void> {
	private String id;

	@Override
	protected Task<Void> createTask() {
		Task<Void> t = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				byte[] handshake = ("connect;" + ConnectionInfo.getName()).getBytes();
				DatagramPacket connect = new DatagramPacket(handshake, handshake.length);
				connect.setAddress(ConnectionInfo.getIp());
				connect.setPort(ConnectionInfo.getPort());
				ConnectionInfo.getSocket().send(connect);
				DatagramPacket response = new DatagramPacket(new byte[1000], 1000);
				ConnectionInfo.getSocket().setSoTimeout(1000);
				System.out.println("Waiting for response....");
				try {
					ConnectionInfo.getSocket().receive(response);
				} catch (SocketTimeoutException e) {
					System.out.println("Trying to connect...");
					return null;
				}
				if (response.getPort() != -1) {
					byte[] data = response.getData();
					String s = new String(data, 0, data.length);
					ConnectionInfo.setId(Integer.parseInt(s.substring(4, 5)));
					System.out.println("Connected " + s);				
				}
				return null;
			}
		};
		return t;
	}
	
	public String getId() {
		return id;
	}
}
