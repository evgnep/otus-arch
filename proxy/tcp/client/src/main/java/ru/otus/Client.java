package ru.otus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Client implements AutoCloseable, Evaluator {
	private final DataInputStream inputStream;
	private final DataOutputStream outputStream;

	public Client(InputStream is, OutputStream os) {
		inputStream = new DataInputStream(new BufferedInputStream(is));
		outputStream = new DataOutputStream(new BufferedOutputStream(os));
	}

	public int add(int a, int b) throws Exception {
		outputStream.writeUTF("add");
		outputStream.writeInt(a);
		outputStream.writeInt(b);
		outputStream.flush();
		return inputStream.readInt();
	}

	@Override
	public void close() throws Exception {
		inputStream.close();
		outputStream.close();
	}
}
