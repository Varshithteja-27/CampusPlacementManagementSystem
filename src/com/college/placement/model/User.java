package com.college.placement.model;

public abstract class User {

	private final int id;
	private final String name;

	public User(int id, String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be blank.");
		}

		this.id = id;
		this.name = name.trim();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public abstract void showProfile();

}
