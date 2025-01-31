package se.lexicon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int idCounter = 250;
    private final int id;
    private final String name;
    private final Person assignedPerson;
    private final LocalDate deadline;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Task(String name, Person assignedPerson, LocalDate deadline) {
        this.id = ++idCounter;
        this.name = name;
        this.assignedPerson = assignedPerson;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Person getAssignedPerson() {
        return assignedPerson;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (Ansvarig: " + assignedPerson + ", Deadline: " + deadline.format(DATE_FORMATTER) + ")";
    }
}
