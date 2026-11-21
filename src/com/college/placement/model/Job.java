package com.college.placement.model;

import static java.lang.System.out;
import java.util.Locale;

public class Job {
	private final int id;
	private final String companyName;
	private final String title;
	private final double minPercentage;

	public Job(int id, String companyName, String title, double minPercentage) {
		if (companyName == null || companyName.isBlank()) {
			throw new IllegalArgumentException("Company name cannot be blank.");
		}
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Title cannot be blank.");
		}
		if (minPercentage < 0.0 || minPercentage > 100.0) {
			throw new IllegalArgumentException("Minimum Percentage must be between 0 to 100.");
		}
		this.id = id;
		this.companyName = companyName.trim();
		this.title = title.trim();
		this.minPercentage = minPercentage;
	}

	public int getId() {
		return id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getTitle() {
		return title;
	}

	public double getMinPercentage() {
		return minPercentage;
	}

	public void showDetails() {
		out.printf(Locale.ROOT, "Job ID: %d | Company: %s | Title: %s | Min Average: %.2f%%%n", id, companyName, title,
				minPercentage);
	}

}
