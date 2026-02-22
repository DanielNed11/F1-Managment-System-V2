package application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private LocalDate dateOfBirth;
    @Getter
    @Setter
    private String nationality;
    @Getter
    @Setter
    private int worldChampionships;
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @Getter
    @Setter
    private String imageUrl;
    @Getter
    @Setter
    @OneToMany(mappedBy = "driver", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RaceDriver> raceDrivers = new ArrayList<>();

    public Driver() {
    }

    public Driver(String name, LocalDate dateOfBirth, String nationality,
                  int worldChampionships, String imageUrl) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.worldChampionships = worldChampionships;
        this.imageUrl = imageUrl;
    }

    public void addRaceDriver(RaceDriver raceDrivers) {
        this.raceDrivers.add(raceDrivers);
        raceDrivers.setDriver(this);
    }

    public void removeRaceDriver(RaceDriver raceDrivers) {
        this.raceDrivers.remove(raceDrivers);
        raceDrivers.setDriver(null);
    }

    public void addRace(Race race, int position) {
        RaceDriver raceDrivers = new RaceDriver(this, race, position);
        addRaceDriver(raceDrivers);
    }

    public void removeRace(Race race) {
        raceDrivers.removeIf(rd -> rd.getRace().equals(race));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver driver)) return false;
        return Objects.equals(id, driver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + nationality + ", Championships: " + worldChampionships + ")";
    }
}
