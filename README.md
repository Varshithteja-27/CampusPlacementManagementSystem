# Campus Placement Management System

A console-based Java application designed to streamline campus recruitment workflows by managing student profiles, job postings, job applications, and placement summary reports with persistent UTF-8 text file storage.

---

## Features

- **Role-Based Console Menus:** Dedicated interactive workflows for **Admin** (placement cell) and **Student** users.
- **Student Management:** Register students, update academic/profile details, and view registered student records.
- **Job Posting Management:** Create new campus job openings, specify eligibility criteria, and list active roles.
- **Application Processing:** Allow students to apply for eligible jobs and enable admins to update application statuses (`PENDING`, `SELECTED`, `REJECTED`).
- **Automated Reporting & Persistence:** Stores all records across structured UTF-8 text files (`students.txt`, `jobs.txt`, `applications.txt`) and generates placement summary reports (`report.txt`).

---

## Project Architecture

```text
CampusPlacementManagementSystem/
├── data/
│   ├── applications.txt
│   ├── jobs.txt
│   ├── report.txt
│   └── students.txt
└── src/
    └── com/college/placement/
        ├── model/
        │   ├── Admin.java
        │   ├── ApplicationStatus.java
        │   ├── Job.java
        │   ├── JobApplication.java
        │   ├── Student.java
        │   └── User.java
        ├── program/
        │   └── MainProgram.java
        ├── service/
        │   └── PlacementService.java
        └── storage/
            └── FileManager.java


```

- **`model`**: Core domain entities and enums (`User`, `Admin`, `Student`, `Job`, `JobApplication`, `ApplicationStatus`).
- **`storage`**: `FileManager` handling UTF-8 file reading, writing, and report export under `data/`.
- **`service`**: `PlacementService` encapsulating business logic, validation, and application state transitions.
- **`program`**: `MainProgram` providing the CLI entry point and user interaction loops.

---

## Prerequisites & How to Run

- **Java Development Kit (JDK):** Java 17 or higher

### Compile and Execute from Terminal

1. Clone the repository:
   ```bash
   git clone [https://github.com/Varshithteja-27/CampusPlacementManagementSystem.git](https://github.com/Varshithteja-27/CampusPlacementManagementSystem.git)
   cd CampusPlacementManagementSystem
   ```

2. Compile all Java source files:
   ```bash
   javac -d bin src/com/college/placement/model/*.java src/com/college/placement/storage/*.java src/com/college/placement/service/*.java src/com/college/placement/program/*.java
   ```

3. Run the application from the project root (so `data/` resolves properly):
   ```bash
   java -cp bin com.college.placement.program.MainProgram
   ```
