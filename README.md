# Simple NU Student Form

This is a Java Swing application for National University student registration.

## Features
- Input student info: ID, Name, Course, Gender, Year
- Buttons: Save, Clear, Exit
- Saves data to PostgreSQL database

## Requirements
- Java 17+
- PostgreSQL JDBC Driver

## How to Run
1. Compile:
   `javac -cp ".;lib/postgresql-42.7.10.jar" src\MainApp.java`
2. Run:
   `java -cp ".;lib/postgresql-42.7.10.jar" MainApp`