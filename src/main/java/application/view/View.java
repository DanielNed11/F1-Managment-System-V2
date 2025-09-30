package application.view;

import application.service.DriverService;
import application.service.F1Service;
import application.service.TrackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class View implements CommandLineRunner {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    DriverService driverService;
    F1Service f1Service;
    TrackService trackService;

    Scanner scanner = new Scanner(System.in);
    boolean running=true;

    private final List<MenuAction> mainMenuItems = new ArrayList<>(List.of(
            new MenuAction("1 Show all drivers", this::showAllDrivers),
            new MenuAction("2 Show all teams", this::showAllF1Teams),
            new MenuAction("3 Show all tracks", this::showAllTracks),
            new MenuAction("4 Show drivers by nationality and/or date of birth", this::filterDrivers)
    ));

    public View(DriverService driverService, F1Service f1Service, TrackService trackService) {
        this.driverService = driverService;
        this.f1Service = f1Service;
        this.trackService = trackService;
        mainMenuItems.addFirst(new MenuAction("Exit", this::leave));
    }

    @Override
    public void run(String... args) {
        show();
    }

    public void show() {
        do {
            showMenu(mainMenuItems);
            // Get the action corresponding to the chosen item and run it
            mainMenuItems.get(parseMenuInput(mainMenuItems.size())).action().run();
        } while (running);
    }

    private void showMenu(List<MenuAction> menu) {
        System.out.println("What do you want to do?");
        for (int i = 0; i < menu.size(); i++) {
            System.out.printf("%d) %s%n", i, menu.get(i).text());
        }
        System.out.print("Please enter the number of your choice: ");
    }

    private int parseMenuInput(int nrAnswers) {
        int choice = -1;
        while (choice == -1) {
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                // leave the choice invalid
            }
            if (choice < 0 ||choice >= nrAnswers){
                System.out.printf("Please enter a number between 0 and %d: " ,(nrAnswers-1));
                choice=-1;
            }
            scanner.nextLine();
        }
        return choice;
    }

    private void leave() {
        System.out.println("\nGoodbye and thanks for all the fish!");
        running=false;
    }

    private void filterDrivers() {
        System.out.print("Nationality (or leave blank): ");
        String nationality = scanner.nextLine().trim();

        System.out.print("Exact date of birth (yyyy/MM/dd) or leave blank: ");
        String dateOfBirthString = scanner.nextLine().trim();
        LocalDate dateOfBirth = null;
        if (!dateOfBirthString.isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(dateOfBirthString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Ignoring DOB filter.");
            }
        }
        LocalDate finalDateOfBirth = dateOfBirth;
        driverService.getDrivers().stream()
                .filter(d -> nationality.isEmpty() || d.getNationality().toLowerCase().contains(nationality.toLowerCase()))
                .filter(d -> finalDateOfBirth == null || d.getDateOfBirth().equals(finalDateOfBirth))
                .forEach(System.out::println);
    }

    private void showAllTracks() {
        trackService.getTracks().forEach(System.out::println);
    }

    private void showAllF1Teams() {
        f1Service.getF1Teams().forEach(System.out::println);
    }

    private void showAllDrivers() {
        driverService.getDrivers().forEach(System.out::println);
    }

    record MenuAction(String text, Runnable action) {}

}