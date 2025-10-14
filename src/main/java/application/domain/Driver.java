package application.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Driver {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private LocalDate dateOfBirth;
    @Getter @Setter
    private String nationality;
    @Getter @Setter
    private int worldChampionships;
    @Getter
    private Team team;
    @Getter @Setter
    private int teamId;
    @Getter @Setter
    private List<Race> races = new ArrayList<>();

    public Driver() {}

    public Driver(String name, LocalDate dateOfBirth, String nationality,
                  int worldChampionships) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.worldChampionships = worldChampionships;
    }

    public void addRace(Race race) {
        if (!races.contains(race)) {
            races.add(race);
        }
    }

    public void removeRace(Race race) {
        races.remove(race);
        race.getDrivers().remove(this);
    }

    public void setTeam(Team newTeam) {
        if (this.team != null && this.team != newTeam) {
            this.team.getDrivers().remove(this);
        }

        this.team = newTeam;

        if (newTeam != null && !newTeam.getDrivers().contains(this)) {
            newTeam.getDrivers().add(this);
        }

        this.teamId = (newTeam != null) ? newTeam.getId() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver driver)) return false;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + nationality + ", Championships: " + worldChampionships+")";
    }
}
