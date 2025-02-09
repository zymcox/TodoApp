package se.lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {
    private final List<Task> taskList = new ArrayList<>();

    public void addTask(Task task) {
        taskList.add(task);
        System.out.println("Uppgift tillagd: " + task);
    }

    public void listTasks() {
        taskList.forEach(System.out::println);
    }

    public void removeTask(int taskId) {
        Optional<Task> taskToRemove = taskList.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();

        if (taskToRemove.isPresent()) {
            taskList.remove(taskToRemove.get());
            System.out.println("Uppgift borttagen.");
        } else {
            System.out.println("Ingen uppgift med det ID hittades.");
        }
    }
}
