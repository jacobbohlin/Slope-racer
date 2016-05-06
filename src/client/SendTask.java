package client;

import java.io.IOException;
import java.net.DatagramPacket;

import javafx.concurrent.Task;

public class SendTask extends Task<Void> {
	private float x,y;
	
	public SendTask(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	protected Void call() throws Exception {
		byte[] message = ("update;" + x + ";" + y).getBytes();
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
