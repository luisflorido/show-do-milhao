package me.luis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server implements Runnable {

  private ServerSocket socket;
  private int port;
  private boolean running = false;
  private static Server server;

  public Server() {
    server = this;
    this.port = getRandomPort();
    try {
      socket = new ServerSocket(this.port);
      System.out.println("Abrindo servidor na porta " + this.port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    new Game().run(this.port);
    running = true;
    try {
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

  public static void send(Object obj) {
    try {
      Socket socket  = new Socket("127.0.0.1", 8080);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(obj);
      oos.close();
    } catch (Exception e) {
      System.out.println("Erro ao enviar objeto!");
      e.printStackTrace();
    }
  }

  private int getRandomPort() {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(65535) + 1;
    return randomInt;
  }

  public static Server getServer() {
    return server;
  }
}
