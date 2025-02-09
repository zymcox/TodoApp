package se.lexicon;

import java.time.LocalDate;

public class Task {

    private static int idCounter = 1;
    private final int id;
    private final String name;
    private final Person assignedPerson;
    private final LocalDate deadline;

    public Task(String name, Person assignedPerson, LocalDate deadline) {
        this.id = idCounter++;
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
        return "Uppgift [ID: " + id + ", Namn: " + name +
                ", Ansvarig: " + assignedPerson.getName() +
                ", Deadline: " + deadline + "]";
    }
}
