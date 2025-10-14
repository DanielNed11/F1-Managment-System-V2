package application.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int foundedYear;
    @Getter @Setter
    private League league;
    @Getter @Setter
    private String teamLogoUrl;
    @Getter @Setter
    private double budgetInMillions;

    @Getter @Setter
    private List<Driver> drivers = new ArrayList<>();

    public Team() {

    }

    public Team(String name, int foundedYear, League status, String teamLogoUrl, double budgetInMillions) {
        this.name = name;
        this.foundedYear = foundedYear;
        this.league = status;
        this.teamLogoUrl = teamLogoUrl;
        this.budgetInMillions = budgetInMillions;
    }


    public void addDriver(Driver driver) {
        if (!drivers.contains(driver)) {
            drivers.add(driver);
            driver.setTeam(this);
            driver.setTeamId(this.id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return id == team.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return name + " (" + ", Founded: " + foundedYear + ")";
    }

}
