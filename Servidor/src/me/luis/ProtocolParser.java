package me.luis;

import java.net.Socket;

public class ProtocolParser implements Runnable {

	private Socket client;
	private Object answer;

	public ProtocolParser(Socket client, Object answer) {
		this.client = client;
		this.answer = answer;
	}

	public void run() {
		try {
			Protocol p = (Protocol) this.answer;
			System.out.println("[PROTOCOLO] TIPO: " + p.getType().name() + " MENSAGEM: " + (String) p.getData());
			switch (p.getType()) {
			case OPEN_CONNECTION:
				System.out.println("open connection");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro ao tratar dados: " + e.getMessage());
		}
	}

}
