package application.repository;

import application.domain.Team;
import application.domain.League;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamRepository implements IRepository<Team> {

    private static final List<Team> teams = new ArrayList<>();

    public TeamRepository() {
        this.add(new Team("Mercedes", 1954, League.Formula_1, "mercedes.png", 10));
        this.add(new Team("Ferrari", 1929, League.Formula_1, "ferrari.png", 30));
        this.add(new Team("Red Bull Racing", 2005, League.Formula_1, "redbull.png", 20));
    }

    @Override
    public List<Team> getAll() {
        return teams;
    }

    @Override
    public Team getById(int id) {
        return teams.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(Team team) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == team.getId()) {
                teams.set(i, team);
                return;
            }
        }
        throw new IllegalArgumentException("Team with id " + team.getId() + " not found");
    }

    @Override
    public void add(Team team) {
        team.setId(teams.size() + 1);
        teams.add(team);
    }
}
