package me.luis;

import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	private ServerSocket socket;
	private int port;
	private static final int DEFAULT_PORT = 8080;
	private boolean running = false;

	public Server() {
		this.port = DEFAULT_PORT;
	}

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		running = true;
		System.out.println("Abrindo servidor na porta " + this.port);
		try {
			socket = new ServerSocket(port);
			System.out.println("Servidor aberto");
			while (running) {
				Socket client = this.socket.accept();
				System.out.println("Mensagem recebida de: " + client.getInetAddress().getHostAddress());
				try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {
					Object obj = ois.readObject();
					if (obj instanceof Protocol) {
						new Thread(new ProtocolParser(client, obj)).start();
					} else {
						OutputStream os = client.getOutputStream();
						os.write("Protocolo inválido!".getBytes());
						os.close();
					}
				} catch (Exception e) {
					System.out.println("Protocolo inválido de: " + client.getInetAddress().getHostAddress());
					OutputStream os = client.getOutputStream();
					os.write("Protocolo inválido!".getBytes());
					os.close();
				}
			}
		} catch (Exception e) {
			running = false;
			System.out.println("Ocorreu um erro no servidor: " + e.getMessage());
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
