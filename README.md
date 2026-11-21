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
