package se.lexicon;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Inga uppgifter att visa.");
        } else {
            System.out.println("Uppgifter:");
            tasks.forEach(task -> System.out.println(task.toString()));
        }
    }

    public void removeTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
        System.out.println("Uppgift borttagen om den existerade.");
    }
}