package client;

import java.net.DatagramPacket;
import java.net.InetAddress;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SendService extends Service<String> {
	private int mouseX, mouseY;
	private final int PORT = ConnectionInfo.PORT;
	private final InetAddress IP = ConnectionInfo.IP;
	private final String NAME = ConnectionInfo.NAME;

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
