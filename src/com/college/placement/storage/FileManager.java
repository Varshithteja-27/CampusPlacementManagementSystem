package com.college.placement.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.college.placement.model.ApplicationStatus;
import com.college.placement.model.Job;
import com.college.placement.model.JobApplication;
import com.college.placement.model.Student;

public class FileManager {
	private static final Path DATA_DIR = Paths.get("data");
	private static final Path STUDENT_FILE = DATA_DIR.resolve("students.txt");
	private static final Path JOBS_FILE = DATA_DIR.resolve("jobs.txt");
	private static final Path APPLICATION_FILE = DATA_DIR.resolve("applications.txt");
	private static final Path REPORT_FILE = DATA_DIR.resolve("report.txt");

	private void ensureDataDirectory() throws IOException {
		if (!Files.exists(DATA_DIR)) {
			Files.createDirectories(DATA_DIR);
		}
	}

	public List<Student> loadStudents() throws IOException {
		List<Student> list = new ArrayList<>();
		if (!Files.exists(STUDENT_FILE)) {
			return list;
		}

		try (BufferedReader reader = Files.newBufferedReader(STUDENT_FILE, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank())
					continue;

				String[] parts = line.split("\\|", -1);
				if (parts.length < 5) {
					throw new IOException("Corrupted Student record: " + line);
				}
				int id = Integer.parseInt(parts[0].trim());
				String name = parts[1].trim();
				String dept = parts[2].trim();

				String[] markToken = parts[3].split(",");
				if (markToken.length != 6) {
					throw new IOException("Invalid marks count for student ID " + id);
				}

				int[] marks = new int[6];
				for (int i = 0; i < 6; i++) {
					marks[i] = Integer.parseInt(markToken[i].trim());
				}

				HashSet<String> skills = new HashSet<>();
				if (!parts[4].isBlank()) {
					String[] skillTokens = parts[4].split(",");
					for (String skill : skillTokens) {
						skills.add(skill.trim());
					}
				}
				list.add(new Student(id, name, dept, marks, skills));
			}
		}
		return list;
	}

	public void saveStudents(List<Student> students) throws IOException {
		ensureDataDirectory();
		try (BufferedWriter writer = Files.newBufferedWriter(STUDENT_FILE, StandardCharsets.UTF_8)) {
			for (Student s : students) {
				int[] m = s.getMarks();
				String marksStr = m[0] + "," + m[1] + "," + m[2] + "," + m[3] + "," + m[4] + "," + m[5];
				String skillsStr = String.join(",", s.getSkills());
				writer.write(
						s.getId() + "|" + s.getName() + "|" + s.getDepartment() + "|" + marksStr + "|" + skillsStr);
				writer.newLine();
			}
		}
	}

	public List<Job> loadJobs() throws IOException {
		List<Job> list = new ArrayList<>();
		if (!Files.exists(JOBS_FILE)) {
			return list;
		}
		try (BufferedReader reader = Files.newBufferedReader(JOBS_FILE, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank())
					continue;

				String[] parts = line.split("\\|", -1);
				if (parts.length < 4) {
					throw new IOException("Corrupted job record: " + line);
				}
				int id = Integer.parseInt(parts[0].trim());
				String company = parts[1].trim();
				String title = parts[2].trim();
				double minPercentage = Double.parseDouble(parts[3].trim());
				list.add(new Job(id, company, title, minPercentage));
			}
		}
		return list;

	}

	public void saveJobs(List<Job> jobs) throws IOException {
		ensureDataDirectory();
		try (BufferedWriter writer = Files.newBufferedWriter(JOBS_FILE, StandardCharsets.UTF_8)) {
			for (Job j : jobs) {
				writer.write(j.getId() + "|" + j.getCompanyName() + "|" + j.getTitle() + "|" + j.getMinPercentage());
				writer.newLine();
			}
		}
	}

	public List<JobApplication> loadApplications() throws IOException {
		List<JobApplication> list = new ArrayList<>();
		if (!Files.exists(APPLICATION_FILE)) {
			return list;
		}
		try (BufferedReader reader = Files.newBufferedReader(APPLICATION_FILE, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank())
					continue;

				String[] parts = line.split("\\|", -1);
				if (parts.length < 4) {
					throw new IOException("Corrupted application record: " + line);
				}
				int id = Integer.parseInt(parts[0].trim());
				int studentId = Integer.parseInt(parts[1].trim());
				int jobId = Integer.parseInt(parts[2].trim());
				ApplicationStatus status = ApplicationStatus.valueOf(parts[3].trim());
				list.add(new JobApplication(id, studentId, jobId, status));
			}
		}
		return list;

	}

	public void saveJobApplications(List<JobApplication> applications) throws IOException {
		ensureDataDirectory();
		try (BufferedWriter writer = Files.newBufferedWriter(APPLICATION_FILE, StandardCharsets.UTF_8)) {
			for (JobApplication app : applications) {
				writer.write(
						app.getId() + "|" + app.getStudentId() + "|" + app.getJobId() + "|" + app.getStatus().name());
				writer.newLine();
			}
		}
	}

	public void saveReport(String reportContent) throws IOException {
		ensureDataDirectory();
		try (BufferedWriter writer = Files.newBufferedWriter(REPORT_FILE, StandardCharsets.UTF_8)) {
			writer.write(reportContent);
		}
	}

}
