package com.college.placement.service;

import static java.lang.System.out;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import com.college.placement.model.ApplicationStatus;
import com.college.placement.model.Job;
import com.college.placement.model.JobApplication;
import com.college.placement.model.Student;
import com.college.placement.model.User;
import com.college.placement.storage.FileManager;

public class PlacementService {

	private final ArrayList<Student> students = new ArrayList<>();
	private final ArrayList<Job> jobs = new ArrayList<>();
	private final ArrayList<JobApplication> applications = new ArrayList<>();
	private final FileManager fileManager = new FileManager();

	public void loadAllData() {
		try {
			students.clear();
			students.addAll(fileManager.loadStudents());
			jobs.clear();
			jobs.addAll(fileManager.loadJobs());
			applications.clear();
			applications.addAll(fileManager.loadApplications());
		} catch (IOException | IllegalArgumentException e) {
			out.println("Error loading data files: " + e.getMessage());
		}
	}

	private int getNextStudentId() {
		int maxId = 0;
		for (Student s : students) {
			if (s.getId() > maxId) {
				maxId = s.getId();
			}
		}
		return maxId + 1;
	}

	private int getNextJobId() {
		int maxId = 0;
		for (Job j : jobs) {
			if (j.getId() > maxId) {
				maxId = j.getId();
			}
		}
		return maxId + 1;
	}

	private int getNextApplicationId() {
		int maxId = 0;
		for (JobApplication a : applications) {
			if (a.getId() > maxId) {
				maxId = a.getId();
			}
		}
		return maxId + 1;
	}

	public Student findStudentById(int id) {
		for (Student s : students) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	public Job findJobById(int id) {
		for (Job j : jobs) {
			if (j.getId() == id) {
				return j;
			}
		}
		return null;
	}

	public JobApplication findApplicationById(int id) {
		for (JobApplication a : applications) {
			if (a.getId() == id) {
				return a;
			}
		}
		return null;
	}

	public void registerStudent(String name, String department, int[] marks, Set<String> skills) {
		int newId = getNextStudentId();
		Student student = new Student(newId, name, department, marks, skills);
		students.add(student);
		try {
			fileManager.saveStudents(students);
			out.println("Record Saved");
			out.println("Student ID: " + student.getId());
			out.printf(Locale.ROOT, "Average: %.2f%n", student.getAverage());

			String skillsDisplay;
			if (student.getSkills().isEmpty()) {
				skillsDisplay = "None";
			} else {
				skillsDisplay = String.join(", ", student.getSkills());
			}

			out.println("Unique skills: " + skillsDisplay);
		} catch (IOException e) {
			students.remove(student);
			out.println("Error saving student record: " + e.getMessage());
		}
	}

	public void viewAllStudents() {
		if (students.isEmpty()) {
			out.println("No records found.");
			return;
		}
		for (Student s : students) {
			User userRef = s;
			userRef.showProfile();
		}
	}

	public void addJob(String companyName, String title, double minPercentage) {
		int newId = getNextJobId();
		Job job = new Job(newId, companyName, title, minPercentage);
		jobs.add(job);

		try {
			fileManager.saveJobs(jobs);
			out.println("Record Saved.");
			out.println("Job ID: " + job.getId());
		} catch (IOException e) {
			jobs.remove(job);
			out.println("Error saving job record: " + e.getMessage());
		}
	}

	public void viewAllJobs() {
		if (jobs.isEmpty()) {
			out.println("No records found.");
			return;
		}
		for (Job j : jobs) {
			j.showDetails();
		}
	}

	public void applyForJob(int studentId, int jobId) {
		Student student = findStudentById(studentId);
		if (student == null) {
			out.println("Student not found.");
			return;
		}

		Job job = findJobById(jobId);
		if (job == null) {
			out.println("Job not found.");
			return;
		}

		for (JobApplication app : applications) {
			if (app.getStudentId() == studentId && app.getJobId() == jobId) {
				out.println("You have already applied for this job.");
				return;
			}
		}

		if (student.getAverage() < job.getMinPercentage()) {
			out.printf(Locale.ROOT,
					"Application refused: Your average (%.2f%%) is below the minimum required (%.2f%%).%n",
					student.getAverage(), job.getMinPercentage());
			return;
		}

		int newId = getNextApplicationId();
		JobApplication application = new JobApplication(newId, studentId, jobId, ApplicationStatus.PENDING);
		applications.add(application);
		try {
			fileManager.saveJobApplications(applications);
			out.println("Record saved.");
			out.println("Application " + application.getId() + " saved. Status: " + application.getStatus());
		} catch (IOException e) {
			applications.remove(application);
			out.println("Error saving application: " + e.getMessage());
		}
	}

	public void viewAllApplications() {
		if (applications.isEmpty()) {
			out.println("No record found.");
			return;
		}
		for (JobApplication app : applications) {
			Student s = findStudentById(app.getStudentId());
			Job j = findJobById(app.getJobId());

			String studentName;
			if (s != null) {
				studentName = s.getName();
			} else {
				studentName = "Unknown";
			}

			String jobTitle;
			if (j != null) {
				jobTitle = j.getTitle() + " (" + j.getCompanyName() + ")";
			} else {
				jobTitle = "Unknown";
			}

			out.println("Application ID: " + app.getId() + " | Student: " + app.getStudentId() + " - " + studentName
					+ " | Job: " + app.getJobId() + " - " + jobTitle + " | Status: " + app.getStatus());
		}
	}

	public void viewStudentApplications(int studentId) {
		boolean found = false;
		for (JobApplication app : applications) {
			if (app.getStudentId() == studentId) {
				found = true;
				Job j = findJobById(app.getJobId());

				String jobInfo;
				if (j != null) {
					jobInfo = j.getTitle() + " at " + j.getCompanyName();
				} else {
					jobInfo = "Job ID " + app.getJobId();
				}
				out.println("Application " + app.getId() + " | " + jobInfo + " | " + app.getStatus());
			}
		}
		if (!found) {
			out.println("No records found.");
		}
	}

	public void updateStatus(int applicationId, ApplicationStatus newStatus) {
		JobApplication app = findApplicationById(applicationId);
		if (app == null) {
			out.println("Application not found.");
			return;
		}
		if (app.getStatus() != ApplicationStatus.PENDING) {
			out.println("Cannot update: Application already " + app.getStatus() + ".");
			return;
		}

		ApplicationStatus previous = app.getStatus();
		if (app.updateStatus(newStatus)) {
			try {
				fileManager.saveJobApplications(applications);
				out.println("Record saved. Application " + applicationId + " updated to " + newStatus + ".");
			} catch (IOException e) {
				out.println("Error saving updated status: " + e.getMessage());
				try {
					var field = JobApplication.class.getDeclaredField("status");
					field.setAccessible(true);
					field.set(app, previous);
				} catch (ReflectiveOperationException ignored) {
				}
			}
		} else {
			out.println("Invalid status update.");
		}
	}

	public void generateReport() {
		int pending = 0;
		int selected = 0;
		int rejected = 0;

		for (JobApplication app : applications) {
			if (app.getStatus() == ApplicationStatus.PENDING) {
				pending++;
			} else if (app.getStatus() == ApplicationStatus.SELECTED) {
				selected++;
			} else if (app.getStatus() == ApplicationStatus.REJECTED) {
				rejected++;
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("=========================================================================\n");
		sb.append("                   CAMPUS PLACEMENT SUMMARY REPORT                       \n");
		sb.append("=========================================================================\n");
		sb.append("Total Registered Students : ").append(students.size()).append("\n");
		sb.append("Total Job Postings        : ").append(jobs.size()).append("\n");
		sb.append("Total Applications        : ").append(applications.size()).append("\n");
		sb.append("  - Pending               : ").append(pending).append("\n");
		sb.append("  - Selected              : ").append(selected).append("\n");
		sb.append("  - Rejected              : ").append(rejected).append("\n");
		sb.append("=========================================================================\n");

		String reportText = sb.toString();
		out.println(reportText);
		try {
			fileManager.saveReport(reportText);
			out.println("Report saved to data/report.txt.");
		} catch (IOException e) {
			out.println("Error saving report: " + e.getMessage());
		}
	}
}
