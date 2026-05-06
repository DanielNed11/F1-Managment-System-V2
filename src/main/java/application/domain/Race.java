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
    @JoinColumn(name = "winner_id", foreignKey = @ForeignKey(name = "fk_race_winner",
        foreignKeyDefinition = "FOREIGN KEY (winner_id) REFERENCES drivers(id) ON DELETE SET NULL"))
    private Driver winner;
    @Getter @Setter
    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RaceDriver> raceDrivers = new ArrayList<>();
    @Getter @Setter
    private boolean hasEnded;

    public Race() {

    }

    public Race(String name, LocalDate date, Driver winner, Track track, List<RaceDriver> raceDrivers) {
        this.name = name;
        this.date = date;
        this.winner = winner;
        this.track = track;
        this.raceDrivers = raceDrivers;
        this.hasEnded = false;
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

    public void addRaceDriver(RaceDriver raceDriver) {
        this.raceDrivers.add(raceDriver);
        raceDriver.setRace(this);
    }

    public void addRaceDriver(Driver driver) {
        RaceDriver raceDriver = new RaceDriver();
        raceDriver.setDriver(driver);
        addRaceDriver(raceDriver);
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
