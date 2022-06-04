package ru.otus;

public class ServerApplication {
	public static void main(String[] args) throws Exception {
		try (var server = new ThreadedServer(8081)) {
			server.start();
		}
	}
}
