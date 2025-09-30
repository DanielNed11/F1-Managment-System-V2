package application.repository;

import application.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataFactory {
    public static List<Driver> drivers = new ArrayList<>();
    public static List<Track> tracks = new ArrayList<>();

    private static void seed() {
        // Tracks
        Track monaco = new Track(1, "Monaco GP", "Monaco", 3.337, 1929);
        Track silverstone = new Track(2, "Silverstone", "UK", 5.891, 1948);
        Track spa = new Track(3, "Spa-Francorchamps", "Belgium", 7.004, 1921);
        Track monza = new Track(4, "Monza", "Italy", 5.793, 1922);
        Track suzuka = new Track(5, "Suzuka", "Japan", 5.807, 1962);

        tracks.addAll(List.of(monaco, silverstone, spa, monza, suzuka));

        // Teams
        F1Team mercedes = new F1Team(1, "Mercedes", 1954, TeamStatus.ACTIVE, true, "mercedes.png", 10);
        F1Team ferrari = new F1Team(2, "Ferrari", 1929, TeamStatus.ACTIVE, true, "ferrari.png", 30);
        F1Team redbull = new F1Team(3, "Red Bull Racing", 2005, TeamStatus.ACTIVE, true, "redbull.png", 20);

        // Drivers
        Driver hamilton = new Driver(1, "Lewis Hamilton", LocalDate.of(1985, 1, 7), "British", 7, ContractStatus.ACTIVE);
        Driver verstappen = new Driver(2, "Max Verstappen", LocalDate.of(1997, 9, 30), "Dutch", 2, ContractStatus.ACTIVE);
        Driver leclerc = new Driver(3, "Charles Leclerc", LocalDate.of(1997, 10, 16), "Monegasque", 0, ContractStatus.ACTIVE);
        Driver sainz = new Driver(4, "Carlos Sainz", LocalDate.of(1994, 9, 1), "Spanish", 0, ContractStatus.ACTIVE);
        Driver bottas = new Driver(5, "Valtteri Bottas", LocalDate.of(1989, 8, 28), "Finnish", 0, ContractStatus.RESERVE);

        // Assign drivers to teams
        mercedes.addDriver(hamilton);
        mercedes.addDriver(bottas);
        ferrari.addDriver(leclerc);
        ferrari.addDriver(sainz);
        redbull.addDriver(verstappen);

        // Assign tracks to drivers
        hamilton.addTrack(monaco);
        hamilton.addTrack(silverstone);
        hamilton.addTrack(spa);

        verstappen.addTrack(spa);
        verstappen.addTrack(suzuka);
        verstappen.addTrack(monza);

        leclerc.addTrack(monaco);
        leclerc.addTrack(monza);
        leclerc.addTrack(silverstone);

        sainz.addTrack(monza);
        sainz.addTrack(spa);

        bottas.addTrack(silverstone);
        bottas.addTrack(suzuka);

        drivers.addAll(List.of(hamilton, verstappen, leclerc, sainz, bottas));
    }

    static {
        seed();
    }
}
