package com.college.placement.program;

import static java.lang.System.out;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.college.placement.model.Admin;
import com.college.placement.model.ApplicationStatus;
import com.college.placement.model.Student;
import com.college.placement.service.PlacementService;

public class MainProgram {

	private static final String ADMIN_PASSWORD = "admin123";

	public static void main(String[] args) {
		PlacementService service = new PlacementService();
		service.loadAllData();

		Scanner scanner = new Scanner(System.in);
		Admin admin = new Admin();
		boolean running = true;

		while (running) {
			out.println("\n================ CAMPUS PLACEMENT MANAGEMENT SYSTEM ================");
			out.println("1. Admin Login");
			out.println("2. Student Registration");
			out.println("3. Student Login");
			out.println("4. Exit");
			int choice = readInt(scanner, "Enter your choice: ");

			if (choice == 1) {
				String password = readNonEmptyString(scanner, "Enter Admin Password: ");
				if (ADMIN_PASSWORD.equals(password)) {
					admin.showProfile();
					runAdminMenu(scanner, service);
				} else {
					out.println("Invalid admin password.");
				}
			} else if (choice == 2) {
				handleStudentRegistration(scanner, service);
			} else if (choice == 3) {
				int studentId = readInt(scanner, "Enter your Student ID: ");
				Student loggedInStudent = service.findStudentById(studentId);
				if (loggedInStudent == null) {
					out.println("Student ID not found. Please register first.");
				} else {
					out.println("Welcome, " + loggedInStudent.getName() + "!");
					runStudentMenu(scanner, service, loggedInStudent);
				}
			} else if (choice == 4) {
				out.println("Exiting Campus Placement Management System. Goodbye!");
				running = false;
			} else {
				out.println("Invalid option. Please choose between 1 and 4.");
			}
		}

		scanner.close();
	}

	private static void runAdminMenu(Scanner scanner, PlacementService service) {
		boolean inAdminMenu = true;
		while (inAdminMenu) {
			out.println("\n--------------------------- ADMIN MENU ---------------------------");
			out.println("1. Add Job Posting");
			out.println("2. View All Students");
			out.println("3. View All Jobs");
			out.println("4. View All Applications");
			out.println("5. Update Application Status (SELECT / REJECT)");
			out.println("6. Generate & Save Placement Report");
			out.println("7. Logout");
			int choice = readInt(scanner, "Enter choice: ");

			if (choice == 1) {
				String company = readNonEmptyString(scanner, "Enter Company Name: ");
				String title = readNonEmptyString(scanner, "Enter Job Title: ");
				double minPct = readDouble(scanner, "Enter Minimum Percentage (0-100): ");
				try {
					service.addJob(company, title, minPct);
				} catch (IllegalArgumentException e) {
					out.println("Error: " + e.getMessage());
				}
			} else if (choice == 2) {
				service.viewAllStudents();
			} else if (choice == 3) {
				service.viewAllJobs();
			} else if (choice == 4) {
				service.viewAllApplications();
			} else if (choice == 5) {
				int appId = readInt(scanner, "Enter Application ID: ");
				out.println("Choose new status: 1. SELECTED | 2. REJECTED");
				int statusChoice = readInt(scanner, "Enter status choice: ");
				if (statusChoice == 1) {
					service.updateStatus(appId, ApplicationStatus.SELECTED);
				} else if (statusChoice == 2) {
					service.updateStatus(appId, ApplicationStatus.REJECTED);
				} else {
					out.println("Invalid status choice.");
				}
			} else if (choice == 6) {
				service.generateReport();
			} else if (choice == 7) {
				inAdminMenu = false;
			} else {
				out.println("Invalid option. Please choose between 1 and 7.");
			}
		}
	}

	private static void runStudentMenu(Scanner scanner, PlacementService service, Student student) {
		boolean inStudentMenu = true;
		while (inStudentMenu) {
			out.println("\n-------------------------- STUDENT MENU --------------------------");
			out.println("1. View My Profile");
			out.println("2. View All Jobs");
			out.println("3. Apply for a Job");
			out.println("4. View My Applications");
			out.println("5. Logout");
			int choice = readInt(scanner, "Enter choice: ");

			if (choice == 1) {
				Student refreshed = service.findStudentById(student.getId());
				if (refreshed != null) {
					refreshed.showProfile();
				}
			} else if (choice == 2) {
				service.viewAllJobs();
			} else if (choice == 3) {
				int jobId = readInt(scanner, "Enter Job ID to apply: ");
				service.applyForJob(student.getId(), jobId);
			} else if (choice == 4) {
				service.viewStudentApplications(student.getId());
			} else if (choice == 5) {
				inStudentMenu = false;
			} else {
				out.println("Invalid option. Please choose between 1 and 5.");
			}
		}
	}

	private static void handleStudentRegistration(Scanner scanner, PlacementService service) {
		String name = readNonEmptyString(scanner, "Enter Student Name: ");
		String department = readNonEmptyString(scanner, "Enter Department: ");

		int[] marks = new int[6];
		for (int i = 0; i < 6; i++) {
			marks[i] = readMark(scanner, "Enter Semester " + (i + 1) + " Marks (0-100): ");
		}

		out.print("Enter Skills (comma-separated, e.g., Java, SQL, Python): ");
		String skillsInput = scanner.nextLine();
		Set<String> skills = new HashSet<>();
		if (!skillsInput.isBlank()) {
			String[] tokens = skillsInput.split(",");
			for (String token : tokens) {
				if (!token.isBlank()) {
					skills.add(token.trim());
				}
			}
		}

		try {
			service.registerStudent(name, department, marks, skills);
		} catch (IllegalArgumentException e) {
			out.println("Registration failed: " + e.getMessage());
		}
	}

	private static int readInt(Scanner scanner, String prompt) {
		while (true) {
			out.print(prompt);
			String line = scanner.nextLine().trim();
			try {
				return Integer.parseInt(line);
			} catch (NumberFormatException e) {
				out.println("Invalid number. Please enter a valid integer.");
			}
		}
	}

	private static int readMark(Scanner scanner, String prompt) {
		while (true) {
			int value = readInt(scanner, prompt);
			if (value >= 0 && value <= 100) {
				return value;
			}
			out.println("Marks must be between 0 and 100.");
		}
	}

	private static double readDouble(Scanner scanner, String prompt) {
		while (true) {
			out.print(prompt);
			String line = scanner.nextLine().trim();
			try {
				return Double.parseDouble(line);
			} catch (NumberFormatException e) {
				out.println("Invalid decimal number. Please try again.");
			}
		}
	}

	private static String readNonEmptyString(Scanner scanner, String prompt) {
		while (true) {
			out.print(prompt);
			String line = scanner.nextLine().trim();
			if (!line.isEmpty()) {
				return line;
			}
			out.println("Input cannot be blank.");
		}
	}
}
