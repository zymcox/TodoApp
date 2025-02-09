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

        // Skapa några standardanvändare
        users.add(new AppUser("admin", "admin123", Role.ROLE_APP_ADMIN));
        users.add(new AppUser("user", "user123", Role.ROLE_APP_USER));

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
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMeny:");
        System.out.println("1. Lägg till en person");
        System.out.println("2. Lägg till en uppgift");
        System.out.println("3. Lista alla uppgifter");
        if (hasRole(Role.ROLE_APP_ADMIN)) {
            System.out.println("4. Ta bort en uppgift");
            System.out.println("6. Tilldela roll till användare");
        }
        System.out.println("5. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    private static void assignUserRole(Scanner scanner) {
        System.out.println("Tillgängliga användare:");
        for (AppUser user : users) {
            System.out.println("Användarnamn: " + user.getUsername() + ", Roll: " + user.getRole());
        }

        System.out.print("Ange användarnamnet för den användare du vill ändra rollen för: ");
        String username = scanner.nextLine();

        AppUser userToModify = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (userToModify == null) {
            System.out.println("Ingen användare hittades med det namnet.");
            return;
        }

        System.out.println("Välj ny roll: ");
        System.out.println("1. ROLE_APP_USER");
        System.out.println("2. ROLE_APP_ADMIN");

        String choice = scanner.nextLine();
        Role newRole = switch (choice) {
            case "1" -> Role.ROLE_APP_USER;
            case "2" -> Role.ROLE_APP_ADMIN;
            default -> {
                System.out.println("Ogiltigt val.");
                yield null;
            }
        };

        if (newRole != null) {
            users.remove(userToModify);
            users.add(new AppUser(userToModify.getUsername(), userToModify.getPassword(), newRole));
            System.out.println("Rollen för användare " + username + " är nu " + newRole);
        }
    }

    private static int getUserChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltig inmatning. Ange ett nummer.");
            return -1;
        }
    }

    private static void authenticateUser(Scanner scanner) {
        System.out.print("Ange användarnamn: ");
        String username = scanner.nextLine();
        System.out.print("Ange lösenord: ");
        String password = scanner.nextLine();

        Optional<AppUser> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Välkommen " + currentUser);
        } else {
            System.out.println("Ogiltigt användarnamn eller lösenord.");
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Ange ett användarnamn: ");
        String username = scanner.nextLine();

        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            System.out.println("Användarnamnet är redan upptaget. Försök igen.");
            return;
        }

        System.out.print("Ange ett lösenord: ");
        String password = scanner.nextLine();

        AppUser newUser = new AppUser(username, password, Role.ROLE_APP_USER);
        users.add(newUser);
        currentUser = newUser;
        System.out.println("Registrering slutförd. Välkommen, " + currentUser.getUsername() + "!");
    }

    private static boolean hasRole(Role role) {
        return currentUser.getRole() == role;
    }

    private static void addPerson(Scanner scanner) {
        System.out.print("Ange personens namn: ");
        String personName = scanner.nextLine();
        Person person = new Person(personName);
        personList.add(person);
        System.out.println("Person tillagd: " + person);
    }

    private static void addTask(Scanner scanner, TaskManager taskManager) {
        if (personList.isEmpty()) {
            System.out.println("Inga personer tillagda. Lägg till en person först.");
            return;
        }
        System.out.print("Ange uppgiftens namn: ");
        String taskName = scanner.nextLine();

        System.out.println("Välj en ansvarig person:");
        for (Person person : personList) {
            System.out.println(person.getId() + ": " + person.getName());
        }

        System.out.print("Ange personens ID: ");
        int personId;
        try {
            personId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt person-ID. Försök igen.");
            return;
        }

        Person assignedPerson = personList.stream()
                .filter(p -> p.getId() == personId)
                .findFirst()
                .orElse(null);

        if (assignedPerson == null) {
            System.out.println("Ingen person hittades med det ID:t.");
            return;
        }

        LocalDate deadline = null;
        while (deadline == null) {
            System.out.print("Ange deadline (yyyy-MM-dd): ");
            String deadlineInput = scanner.nextLine();
            if (deadlineInput.isBlank()) {
                System.out.println("Deadline får inte vara tom. Försök igen.");
                continue;
            }

            try {
                deadline = LocalDate.parse(deadlineInput);
            } catch (DateTimeParseException e) {
                System.out.println("Ogiltigt datumformat. Ange datum enligt formatet yyyy-MM-dd.");
            }
        }

        taskManager.addTask(new Task(taskName, assignedPerson, deadline));
        System.out.println("Uppgift tillagd!");
    }

    private static void removeTask(Scanner scanner, TaskManager taskManager) {
        System.out.print("Ange uppgiftens ID att ta bort: ");
        int id = Integer.parseInt(scanner.nextLine());
        taskManager.removeTask(id);
    }
}
