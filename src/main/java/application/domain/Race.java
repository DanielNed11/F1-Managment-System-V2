package application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "races")
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private LocalDate date;
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id")
    private Track track;
    @Getter @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "winner_id")
    private Driver winner;
    @Getter @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "race_drivers",
            joinColumns = @JoinColumn(name = "race_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<Driver> drivers = new ArrayList<>();
    @Getter @Setter
    private boolean hasEnded;

    public Race() {

    }

    public Race(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    @PrePersist
    @PreUpdate
    public void updateHasEnded() {
        this.hasEnded = date != null && date.isBefore(LocalDate.now());
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
