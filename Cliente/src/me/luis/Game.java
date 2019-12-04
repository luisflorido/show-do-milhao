package me.luis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {

  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Game game = new Game();

  public void run(Integer port) {
    System.out.println("Insira seu nome: ");
    try {
      String name = reader.readLine();
      Protocol protocol = new Protocol(Protocol.Types.OPEN_CONNECTION, name + "," + port + "," + Main.token);
      Server.send(protocol);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void answer() {
    System.out.println("\nDigite sua resposta (0-1-2-3):");
    try {
      String resposta = reader.readLine();
      Protocol protocol = new Protocol(Protocol.Types.ANSWER_QUESTION, Main.token + "," + resposta);
      Server.send(protocol);
    } catch (Exception e) {
      System.out.println("Erro ao enviar resposta!" + e.getMessage());
    }
  }

  public static Game getGame() {
    return game;
  }
}
