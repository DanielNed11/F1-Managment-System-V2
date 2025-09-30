import java.util.ArrayList;
import java.util.List;

public class Track {
    private int id;
    private String name;
    private String location;
    private double lengthKm;
    private int openedYear;

    private List<Driver> drivers = new ArrayList<>();

    public Track(int id, String name, String location, double lengthKm, int openedYear) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.lengthKm = lengthKm;
        this.openedYear = openedYear;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getLengthKm() { return lengthKm; }
    public int getOpenedYear() { return openedYear; }
    public List<Driver> getDrivers() { return drivers; }

    @Override
    public String toString() {
        return name + " (" + location + ", " + lengthKm + " km)";
    }
}
