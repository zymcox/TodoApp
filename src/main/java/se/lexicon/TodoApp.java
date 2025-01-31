package se.lexicon;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApp {
    private static final List<Person> personList = new ArrayList<>();

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Välkommen till Todo-appen!");
        while (true) {
            System.out.println("\nMeny:");
            System.out.println("1. Lägg till en person");
            System.out.println("2. Lägg till en uppgift");
            System.out.println("3. Lista alla uppgifter");
            System.out.println("4. Ta bort en uppgift");
            System.out.println("5. Avsluta");
            System.out.print("Välj ett alternativ: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Ange personens namn: ");
                    String personName = scanner.nextLine();
                    Person person = new Person(personName);
                    personList.add(person);
                    System.out.println("Person tillagd: " + person);
                }
                case 2 -> {
                    if (personList.isEmpty()) {
                        System.out.println("Inga personer tillagda. Lägg till en person först.");
                        break;
                    }
                    System.out.print("Ange uppgiftens namn: ");
                    String taskName = scanner.nextLine();

                    System.out.println("Välj en ansvarig person:");
                    for (Person person : personList) {
                        System.out.println(person.getId() + ": " + person.getName());
                    }

                    System.out.print("Ange personens ID: ");
                    int personId = Integer.parseInt(scanner.nextLine());
                    Person assignedPerson = personList.stream()
                            .filter(p -> p.getId() == personId)
                            .findFirst()
                            .orElse(null);

                    if (assignedPerson == null) {
                        System.out.println("Ogiltigt person-ID.");
                        break;
                    }

                    System.out.print("Ange deadline (yyyy-MM-dd): ");
                    LocalDate deadline;
                    try {
                        deadline = LocalDate.parse(scanner.nextLine());
                    } catch (DateTimeParseException e) {
                        System.out.println("Ogiltigt datumformat. Försök igen.");
                        break;
                    }

                    taskManager.addTask(new Task(taskName, assignedPerson, deadline));
                    System.out.println("Uppgift tillagd!");
                }
                case 3 -> taskManager.listTasks();
                case 4 -> {
                    System.out.print("Ange uppgiftens ID att ta bort: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    taskManager.removeTask(id);
                }
                case 5 -> {
                    System.out.println("Avslutar...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
}