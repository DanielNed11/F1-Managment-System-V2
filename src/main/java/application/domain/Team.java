package application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team {
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
    private int foundedYear;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private League league;
    @Getter
    @Setter
    private String teamLogoUrl;
    @Getter
    @Setter
    private double budgetInMillions;

    @Getter
    @Setter
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
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
        drivers.add(driver);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(id, team.id);
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
