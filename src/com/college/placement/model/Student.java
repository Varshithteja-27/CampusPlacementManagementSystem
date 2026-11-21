package com.college.placement.model;

import static java.lang.System.out;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Student extends User {
	private final String department;
	private final int[] marks;
	private final HashSet<String> skills;

	public Student(int id, String name, String department, int[] marks, Set<String> skills) {
		super(id, name);

		if (department == null || department.isBlank()) {
			throw new IllegalArgumentException("Department cannot be blank.");
		}
		if (marks == null || marks.length != 6) {
			throw new IllegalArgumentException("Exactly 6 semester marks are required.");
		}
		for (int mark : marks) {
			if (mark < 0 || mark > 100) {
				throw new IllegalArgumentException("Each mark must be between 0 and 100.");
			}
		}

		this.department = department.trim();
		this.marks = marks.clone();
		this.skills = new HashSet<>();

		if (skills != null) {
			for (String skill : skills) {
				if (skill != null && !skill.isBlank()) {
					this.skills.add(skill.trim().toUpperCase(Locale.ROOT));
				}
			}
		}
	}

	public String getDepartment() {
		return department;
	}

	public int[] getMarks() {
		return marks.clone();
	}

	public HashSet<String> getSkills() {
		return new HashSet<String>(skills);
	}

	public double getAverage() {
		int total = 0;
		for (int mark : marks) {
			total = total + mark;
		}
		return (double) total / marks.length;
	}

	@Override
	public void showProfile() {
		out.println("=========================================================================");
		out.println("Student ID    : " + getId());
		out.println("Name          : " + getName());
		out.println("Department    : " + department);
		out.print("Marks         : ");

		for (int i = 0; i < marks.length; i++) {
			out.print("Sem" + (i + 1) + "=" + marks[i]);
			if (i < marks.length - 1) {
				out.print(", ");
			}
		}
		out.println();
		out.printf(Locale.ROOT, "Average       : %.2f%%%n", getAverage());

		String skillsDisplay;
		if (skills.isEmpty()) {
			skillsDisplay = "None";
		} else {
			skillsDisplay = String.join(", ", skills);
		}
		out.println("Skills        : " + skillsDisplay);
		out.println("=========================================================================");
	}
}
