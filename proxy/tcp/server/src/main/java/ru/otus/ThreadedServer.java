package ru.otus;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer implements AutoCloseable {
	private final ServerSocket serverSocket;

	public ThreadedServer(int port) throws Exception {
		serverSocket = new ServerSocket(port);
	}

	public void start() throws Exception {
		System.out.println("listen");

		while (true) {
			var socket = serverSocket.accept();
			new ClientHandlerThread(socket).start();
		}
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}

	private static class ClientHandlerThread extends Thread {
		private final Socket socket;

		ClientHandlerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				System.out.println(socket.getPort() + " accepted");
				try (var client = new ClientHandler(socket)) {
					client.run();
				}
			}
			catch (Exception e) {
				System.out.println(socket.getPort() + " exception: " + e);
			}
			finally {
				try {
					socket.close();
				}
				catch (Exception e) {
				}
			}
		}
	}
}
