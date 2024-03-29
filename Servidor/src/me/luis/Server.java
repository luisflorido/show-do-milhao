package me.luis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    new Game();
    running = true;
    System.out.println("Abrindo servidor na porta " + this.port);
    try {
      socket = new ServerSocket(port);
      System.out.println("Servidor aberto");
      while (running) {
        Socket client = this.socket.accept();
//        System.out.println("Mensagem recebida de: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {
          Object obj = ois.readObject();
          if (obj instanceof Protocol) {
            new ProtocolParser(client, obj).run();
          }
        } catch (Exception e) {
          System.out.println("Protocolo inválido de: " + client.getInetAddress().getHostAddress());
        }
      }
    } catch (Exception e) {
      running = false;
      System.out.println("Ocorreu um erro no servidor: " + e.getMessage());
    }
  }

  public static void send(String ip, Integer port, Object obj) {
    try {
      Socket socket = new Socket(ip, port);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(obj);
      oos.close();
    } catch (Exception e) {
      System.out.println("Erro ao enviar objeto!");
      e.printStackTrace();
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
