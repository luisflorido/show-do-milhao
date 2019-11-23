package me.luis;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
		Server server = new Server();
		server.run();
	}
}
