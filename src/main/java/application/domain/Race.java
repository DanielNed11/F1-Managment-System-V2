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
    private Integer id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private LocalDate date;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;
    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Driver winner;
    @Getter @Setter
    @OneToMany(mappedBy = "race", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DriverRace> driverRaces = new ArrayList<>();
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

    public void addDriverRace(DriverRace driverRace) {
        driverRaces.add(driverRace);
        driverRace.setRace(this);
    }

    public void removeDriverRace(DriverRace driverRace) {
        driverRaces.remove(driverRace);
        driverRace.setRace(null);
    }

    public void addDriver(Driver driver) {
        DriverRace driverRace = new DriverRace(driver, this);
        addDriverRace(driverRace);
    }

    public void removeDriver(Driver driver) {
        driverRaces.removeIf(dr -> dr.getDriver().equals(driver));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race race)) return false;
        return Objects.equals(id, race.id);
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
