package se.lexicon;

import java.time.LocalDate;

public class Task {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private Person assignedPerson;
    private LocalDate deadline;

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
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", assignedPerson=" + assignedPerson +
                ", deadline=" + deadline +
                '}';
    }
}
