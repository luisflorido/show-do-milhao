package me.luis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

	private boolean running;
	private List<Person> persons;
	private Map<String, List<String>> questions = new HashMap<>();
	private static final String DEFAULT_FILENAME = "questions.txt";

	public void run() {
		System.out.println("Carregando jogo...");
		running = true;
		persons = new ArrayList<>();
		loadQuestions();
	}

	private void loadQuestions() {
		List<String> list = readFile(DEFAULT_FILENAME);
		list.stream().forEach(e -> {
			String[] parse = e.split(":");
			List<String> answers = Arrays.asList(Arrays.copyOfRange(parse, 1, parse.length));
			questions.put(parse[0], answers);
		});
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
			System.err.format("Ocorreu um erro ao carregar questões: " + e.getMessage());
			return null;
		}
	}
	
	private String clientAnswer(String answer,String question){
		try {
			loadQuestions();
			if() {
				
			}
			
		} catch (Exception e) {
			System.err.format("Ocorreu um erro ao carregar questões: " + e.getMessage());
			return null;
		}
	}
}
