package ru.otus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class ClientHandler implements AutoCloseable {
	private final DataInputStream inputStream;
	private final DataOutputStream outputStream;

	public ClientHandler(Socket socket) throws Exception {
		inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	public void run() throws Exception {
		while (true) {
			var command = inputStream.readUTF();
			switch (command) {
				case "add":
					var a = inputStream.readInt();
					var b = inputStream.readInt();
					System.out.println("add: " + a + ", " + b);
					outputStream.writeInt(a + b);
					outputStream.flush();
					break;
				default:
					throw new UnsupportedOperationException("Unknown command: " + command);
			}
		}
	}

	@Override
	public void close() throws Exception {
		inputStream.close();
		outputStream.close();
	}
}
