package application.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Track {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String location;
    @Getter @Setter
    private double lengthKm;
    @Getter @Setter
    private int openedYear;

    public Track() {

    }

    public Track(String name, String location, double lengthKm, int openedYear) {
        this.name = name;
        this.location = location;
        this.lengthKm = lengthKm;
        this.openedYear = openedYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track track)) return false;
        return id == track.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + location + ", " + lengthKm + " km)";
    }
}
