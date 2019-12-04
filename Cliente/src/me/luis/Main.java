package me.luis;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {

  public static String token = generateToken();

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

  private static String generateToken() {
    byte[] array = new byte[7];
    new Random().nextBytes(array);
    String generatedString = new String(array, StandardCharsets.UTF_8);

    return generatedString;
  }
}
