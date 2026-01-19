package application.repository.local;

import application.domain.Team;
import application.domain.League;
import application.repository.ITeamRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("collection")
public class TeamRepository implements ITeamRepository {

    private static final List<Team> teams = new ArrayList<>();

    public TeamRepository() {
        this.add(new Team("Mercedes", 1954, League.Formula_1,
                "/img/mercedesLogo.png", 10));
        this.add(new Team("Ferrari", 1929, League.Formula_1,
                "/img/ferrariLogo.png", 30));
        this.add(new Team("Red Bull Racing", 2005, League.Formula_1,
                "/img/redBullLogo.png", 20));
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
    public void add(Team team) {
        team.setId(teams.size() + 1);
        teams.add(team);
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
    public void delete(int id) {
        teams.removeIf(t -> t.getId() == id);
    }
}
