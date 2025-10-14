package application.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Race {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private LocalDate date;
    @Getter
    private Track track;
    @Getter @Setter
    private Driver winner;
    @Getter @Setter
    private List<Driver> drivers = new ArrayList<>();
    @Getter @Setter
    private boolean hasEnded;

    public Race() {

    }

    public Race(String name, LocalDate date, boolean hasEnded) {
        this.name = name;
        this.date = date;
        this.hasEnded = hasEnded;
    }

    public void setTrack(Track track) {
        if (track != null) {
            this.track = track;
        }
    }

    public void removeDriver(Driver driver) {
        drivers.remove(driver);
        driver.removeRace(this);
    }

    public void addDriver(Driver driver) {
        if (drivers.contains(driver)) return;
        drivers.add(driver);
        driver.addRace(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race race)) return false;
        return id == race.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Id: %d, Name: %s", id, name);
    }

}
