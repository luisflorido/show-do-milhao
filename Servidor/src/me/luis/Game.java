package me.luis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

  private static Game game;
  private String pergunta;
  private String resposta;
  private Integer answered = 0;
  private Integer winners = 0;
  private List<String> alternativas;
  private boolean running;
  private List<Person> persons;
  private Map<String, List<String>> questions = new HashMap<>();
  private static final String DEFAULT_FILENAME = "questions.txt";

  public Game() {
    game = this;
    System.out.println("Carregando jogo...");
    running = false;
    persons = new ArrayList<>();
    loadQuestions();
  }

  public void addPerson(String ip, Integer port, String token, String name) {
    Person p = new Person(ip, port, token, name);
    persons.add(p);
  }

  private void loadQuestions() {
    List<String> list = readFile(DEFAULT_FILENAME);
    list.forEach(e -> {
      String[] parse = e.split(":");
      List<String> answers = Arrays.asList(Arrays.copyOfRange(parse, 1, parse.length));
      questions.put(parse[0], answers);
    });
    System.out.println("Questões carregadas.");
  }

  private List<String> readFile(String filename) {
    List<String> records = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = reader.readLine()) != null) {
        records.add(line);
      }
      reader.close();
      return records;
    } catch (Exception e) {
      System.err.format("Ocorreu um erro ao carregar quest�es: " + e.getMessage());
      return null;
    }
  }

  public void start() {
    setTimeout(() -> {
      setQuestionAndAnswer();
      persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.START_GAME, "O jogo começou, primeira pergunta: \n" + this.pergunta + "\nRespostas: \n" + String.join("\n", this.alternativas))));
    }, 5000);
  }

  private void setQuestionAndAnswer() {
    List<String> perguntas = new ArrayList<>(questions.keySet());
    Collections.shuffle(perguntas);
    this.pergunta = perguntas.get(0);
    this.resposta = questions.get(this.pergunta).get(0);
    this.alternativas = questions.get(this.pergunta);
    Collections.shuffle(this.alternativas);
  }

  public static void setTimeout(Runnable runnable, int delay) {
    new Thread(() -> {
      try {
        Thread.sleep(delay);
        runnable.run();
      } catch (Exception e) {
        System.err.println(e);
      }
    }).start();
  }

  public void answer(Person p, String resposta) {
    if (this.resposta.equals(this.alternativas.get(Integer.parseInt(resposta)))) {
      Integer points = getPointsOnRight();
      getPerson(p.getIp(), p.getToken()).increasePoints(points);
      persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.MESSAGE, p.getName() + " acertou e ganhou " + points + " pontos.")));
    } else {
      persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.MESSAGE, p.getName() + " errou e ganhou 0 pontos.")));
    }
    answered++;
    System.out.println(answered + " " + persons.size() + " " + answered.equals(persons.size()));
    if (answered.equals(persons.size())) {
      var winner = getWinner();
      if (winner != null) {
        persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.END_GAME, "\n" + winner.getName() + " ganhou o jogo com " + winner.getPoints() + " pontos.")));
        System.exit(0);
      } else {
        answered = 0;
        winners = 0;
        setQuestionAndAnswer();
        persons.forEach(e -> Server.send(e.getIp(), e.getPort(), new Protocol(Protocol.Types.START_GAME, "\nPergunta: " + this.pergunta + "\nRespostas: \n" + String.join("\n", this.alternativas))));
      }
    }
  }

  private Integer getPointsOnRight() {
    winners++;
    return 10 - (winners - 1);
  }

  public Person getPerson(String ip, String token) {
    return persons.stream().filter(e -> e.getIp().equals(ip) && e.getToken().equals(token)).findFirst().orElse(null);
  }

  public Person getWinner() {
    return persons.stream().filter(e -> e.getPoints() >= 11).max(Comparator.comparing(Person::getPoints)).orElse(null);
  }

  public boolean isRunning() {
    return running;
  }

  public static Game getGame() {
    return game;
  }

  public List<Person> getPersons() {
    return persons;
  }
}
