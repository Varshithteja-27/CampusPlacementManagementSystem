package com.college.placement.model;

public class Admin extends User {

	public Admin() {
		super(0, "College Admin");

	}

	@Override
	public void showProfile() {
		System.out.println("Admin ID: " + getId() + " | Name: " + getName());

	}

}
