package se.lexicon;

import se.lexicon.AppUser.Role;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TodoApp {
    private static final List<Person> personList = new ArrayList<>();
    private static final List<AppUser> users = new ArrayList<>();
    private static AppUser currentUser;

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        // Fördefinierad administratör "uffe"
        users.add(new AppUser("uffe", "abc", Role.ROLE_APP_ADMIN));

        while (currentUser == null) {
            System.out.println("\n1. Logga in");
            System.out.println("2. Registrera ny användare");
            System.out.print("Välj ett alternativ: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> authenticateUser(scanner);
                case "2" -> registerUser(scanner);
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }

        while (true) {
            printMenu();

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1 -> addPerson(scanner);
                case 2 -> addTask(scanner, taskManager);
                case 3 -> taskManager.listTasks();
                case 4 -> {
                    if (hasRole(Role.ROLE_APP_ADMIN)) {
                        removeTask(scanner, taskManager);
                    } else {
                        System.out.println("Endast administratörer kan ta bort uppgifter.");
                    }
                }
                case 5 -> {
                    System.out.println("Avslutar...");
                    scanner.close();
                    return;
                }
                case 6 -> {
                    if (hasRole(Role.ROLE_APP_ADMIN)) {
                        assignUserRole(scanner);
                    } else {
                        System.out.println("Endast administratörer kan tilldela roller.");
                    }
                }
                case 7 -> {
                    if (hasRole(Role.ROLE_APP_ADMIN)) {
                        editUser(scanner);
                    } else {
                        System.out.println("Endast administratörer kan redigera användare.");
                    }
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }

    private static void authenticateUser(Scanner scanner) {
        System.out.print("Ange användarnamn: ");
        String username = scanner.nextLine().trim();
        System.out.print("Ange lösenord: ");
        String password = scanner.nextLine().trim();

        Optional<AppUser> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Inloggad som " + currentUser.getUsername());
        } else {
            System.out.println("Felaktigt användarnamn eller lösenord.");
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Ange ett användarnamn: ");
        String username = scanner.nextLine().trim();
        System.out.print("Ange ett lösenord: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Användarnamn och lösenord får inte vara tomma.");
            return;
        }

        boolean userExists = users.stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if (userExists) {
            System.out.println("Användarnamnet är redan upptaget.");
        } else {
            users.add(new AppUser(username, password, Role.ROLE_APP_USER));
            System.out.println("Användare registrerad.");
        }
    }

    private static int getUserChoice(Scanner scanner) {
        try {
            System.out.print("Ditt val: ");
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt val. Ange ett nummer.");
            return -1;
        }
    }

    private static void addPerson(Scanner scanner) {
        System.out.print("Ange personens namn: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Namnet får inte vara tomt.");
            return;
        }

        personList.add(new Person(name));
        System.out.println("Person tillagd.");
    }

    private static void addTask(Scanner scanner, TaskManager taskManager) {
        System.out.print("Ange uppgiftens namn: ");
        String taskName = scanner.nextLine().trim();

        if (taskName.isEmpty()) {
            System.out.println("Uppgiftens namn får inte vara tomt.");
            return;
        }

        System.out.println("Tillgängliga personer:");
        personList.forEach(System.out::println);

        System.out.print("Ange ID för tilldelad person: ");
        int personId = getUserChoice(scanner);

        Optional<Person> personOptional = personList.stream()
                .filter(person -> person.getId() == personId)
                .findFirst();

        if (personOptional.isEmpty()) {
            System.out.println("Ogiltigt person-ID.");
            return;
        }

        System.out.print("Ange deadline (ÅÅÅÅ-MM-DD): ");
        try {
            LocalDate deadline = LocalDate.parse(scanner.nextLine().trim());
            taskManager.addTask(new Task(taskName, personOptional.get(), deadline));
        } catch (DateTimeParseException e) {
            System.out.println("Ogiltigt datumformat.");
        }
    }

    private static boolean hasRole(Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    private static void removeTask(Scanner scanner, TaskManager taskManager) {
        System.out.print("Ange ID för uppgift att ta bort: ");
        int taskId = getUserChoice(scanner);
        if (taskId > 0) {
            taskManager.removeTask(taskId);
        } else {
            System.out.println("Ogiltigt task-ID.");
        }
    }

    private static void assignUserRole(Scanner scanner) {
        System.out.print("Ange användarnamn för att ändra roll: ");
        String username = scanner.nextLine().trim();

        Optional<AppUser> userOptional = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if (userOptional.isEmpty()) {
            System.out.println("Användare hittades inte.");
            return;
        }

        AppUser user = userOptional.get();
        System.out.println("Välj ny roll (1: ROLE_APP_USER, 2: ROLE_APP_ADMIN): ");
        int roleChoice = getUserChoice(scanner);

        switch (roleChoice) {
            case 1 -> user.setRole(Role.ROLE_APP_USER);
            case 2 -> user.setRole(Role.ROLE_APP_ADMIN);
            default -> System.out.println("Ogiltigt val.");
        }

        System.out.println("Roll uppdaterad för " + user.getUsername());
    }

    private static void editUser(Scanner scanner) {
        System.out.print("Ange användarnamn att redigera: ");
        String username = scanner.nextLine().trim();

        Optional<AppUser> userOptional = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if (userOptional.isEmpty()) {
            System.out.println("Användare hittades inte.");
            return;
        }

        AppUser user = userOptional.get();
        System.out.print("Ange nytt lösenord: ");
        String newPassword = scanner.nextLine().trim();

        if (newPassword.isEmpty()) {
            System.out.println("Lösenordet får inte vara tomt.");
        } else {
            user.setPassword(newPassword);
            System.out.println("Lösenord uppdaterat.");
        }
    }

    private static void printMenu() {
        System.out.println("\nMeny:");
        System.out.println("1. Lägg till person");
        System.out.println("2. Lägg till uppgift");
        System.out.println("3. Lista alla uppgifter");
        System.out.println("4. Ta bort uppgift (Endast admin)");
        System.out.println("5. Avsluta");
        System.out.println("6. Tilldela roll (Endast admin)");
        System.out.println("7. Redigera användare (Endast admin)");
    }
}