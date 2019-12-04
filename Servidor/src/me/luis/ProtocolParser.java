package me.luis;

import java.net.Socket;
import java.util.List;

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
      String ip = client.getInetAddress().getHostAddress();

      Protocol protocol = (Protocol) this.answer;
//      System.out.println("[PROTOCOLO] TIPO: " + protocol.getType().name() + " MENSAGEM: " + (String) protocol.getData());
      switch (protocol.getType()) {
        case OPEN_CONNECTION:
          String[] split = ((String) protocol.getData()).split(",");
          String nome = split[0];
          Integer port = Integer.parseInt(split[1]);
          String token = split[2];

          if (game.isRunning()) {
            Server.send(ip, port, new Protocol(Protocol.Types.MESSAGE, "O jogo já está em andamento!"));
          } else {
            game.addPerson(ip, port, token, nome);
            List<Person> persons = game.getPersons();

            persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.MESSAGE, nome + " entrou no jogo! " + persons.size() + "/2")));

            if (persons.size() >= 2) {
              persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.MESSAGE, "O jogo começara em 30s...")));
              game.start();
            }
          }
          break;
        case ANSWER_QUESTION:
          split = (String[])((String) protocol.getData()).split(",");
          token = split[0];
          String resposta = split[1];
          Person p = game.getPerson(ip, token);
          game.answer(p, resposta);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      System.out.println("Ocorreu um erro ao tratar dados: " + e.getMessage());
    }
  }

}
