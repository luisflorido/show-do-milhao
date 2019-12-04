package me.luis;

public class Person {

  private String ip;
  private Integer port;
  private String name, token;
  private Integer points;

  public Person(String ip, Integer port, String token, String name) {
    this.ip = ip;
    this.port = port;
    points = 0;
    this.name = name;
    this.token = token;
  }

  public String getIp() {
    return ip;
  }

  public Integer getPort() {
    return port;
  }

  public String getName() {
    return name;
  }

  public String getToken() {
    return token;
  }

  public void increasePoints(Integer point) {
    this.points += point;
  }

  public Integer getPoints() {
    return this.points;
  }
}
