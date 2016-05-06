package client;

import java.net.DatagramPacket;
import java.net.InetAddress;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SendService extends Service<Void> {
	private int mouseX, mouseY;

	@Override
	protected Task<Void> createTask() {

		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				return null;
			}
		};
		
	}

	public void setMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

}
