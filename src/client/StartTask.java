package client;

import java.io.IOException;
import java.net.DatagramPacket;

import javafx.concurrent.Task;

public class StartTask extends Task<Void> {
	
	@Override
	protected Void call() throws Exception {
		byte[] message = ("start").getBytes();
		DatagramPacket dp = new DatagramPacket(message, message.length);
		dp.setAddress(ConnectionInfo.getIp());
		dp.setPort(ConnectionInfo.getPort());
		try {
			ConnectionInfo.getSocket().send(dp);
		} catch(IOException e) {
			System.out.println("Failed to send packet...");
			e.printStackTrace();
		}
		return null;
	}

}