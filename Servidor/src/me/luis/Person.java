package me.luis;

import java.util.List;

public class Person {

	private String ip;
	private String name;

	public Person(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Person findPerson(List<Person> peoples, String ip) {
		return peoples.stream().filter(e -> e.ip.equals(ip)).findFirst().orElse(null);
	}
}
