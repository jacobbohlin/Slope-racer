package client;

import java.net.DatagramPacket;
import java.net.InetAddress;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SendService extends Service<String> {
	private int mouseX, mouseY;

	@Override
	protected Task<String> createTask() {

		return new Task<String>() {

			@Override
			protected String call() throws Exception {
				return null;
			}
		};
	}

	public void setMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

}
