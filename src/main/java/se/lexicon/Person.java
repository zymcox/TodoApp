package se.lexicon;

public class Person {
    private static int idCounter = 0;
    private final int id;
    private final String name;

    public Person(String name) {
        this.id = ++idCounter;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}