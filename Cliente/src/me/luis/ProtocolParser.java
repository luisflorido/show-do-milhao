package me.luis;

import java.net.Socket;

public class ProtocolParser {

  private Socket client;
  private Object answer;
  private Game game = Game.getGame();

  public ProtocolParser(Socket client, Object answer) {
    this.client = client;
    this.answer = answer;
  }

  public void run() {
    try {
      Protocol p = (Protocol) this.answer;
//      System.out.println("PROTOCOLO " + p.getType());
      switch (p.getType()) {
        case MESSAGE:
          System.out.println("[MENSAGEM]: " + p.getData());
          break;
        case START_GAME:
          System.out.println(p.getData());
          game.answer();
          break;
        case END_GAME:
          System.out.println(p.getData());
          System.exit(0);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      System.out.println("Ocorreu um erro ao tratar dados: " + e.getMessage());
    }
  }

}
