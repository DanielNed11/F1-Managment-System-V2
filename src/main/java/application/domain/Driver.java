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
    @OneToMany(mappedBy = "driver", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DriverRace> driverRaces = new ArrayList<>();

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

    public void addDriverRace(DriverRace driverRace) {
        driverRaces.add(driverRace);
        driverRace.setDriver(this);
    }

    public void removeDriverRace(DriverRace driverRace) {
        driverRaces.remove(driverRace);
        driverRace.setDriver(null);
    }

    public void addRace(Race race) {
        DriverRace driverRace = new DriverRace(this, race);
        addDriverRace(driverRace);
    }

    public void removeRace(Race race) {
        driverRaces.removeIf(dr -> dr.getRace().equals(race));
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
