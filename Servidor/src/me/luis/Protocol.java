package me.luis;

import java.io.Serializable;

public class Protocol implements Serializable {

  private static final long serialVersionUID = 1L;
  private Types type;
  private Object data;

  public Protocol(Types type, Object data) {
    this.type = type;
    this.data = data;
  }

  public Types getType() {
    return type;
  }

  public Object getData() {
    return data;
  }

  enum Types {
    OPEN_CONNECTION, MESSAGE, ANSWER_QUESTION, START_GAME, END_GAME
  }
}
