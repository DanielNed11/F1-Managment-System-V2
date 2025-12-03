package application.repository.jdbc;

import application.domain.League;
import application.domain.Team;
import application.repository.IRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository("teamRepository")
@Profile("jdbc")
@Primary
public class TeamJdbcRepository implements IRepository<Team> {

    private final JdbcClient jdbcClient;

    public TeamJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Team> getAll() {
        String sql = "SELECT * FROM teams";
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setFoundedYear(rs.getInt("founded_year"));
                    team.setLeague(League.valueOf(rs.getString("league")));
                    team.setTeamLogoUrl(rs.getString("team_logo_url"));
                    team.setBudgetInMillions(rs.getDouble("budget_in_millions"));
                    return team;
                })
                .list();
    }

    @Override
    public Team getById(int id) {
        String sql = "SELECT * FROM teams WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setFoundedYear(rs.getInt("founded_year"));
                    team.setLeague(League.valueOf(rs.getString("league")));
                    team.setTeamLogoUrl(rs.getString("team_logo_url"));
                    team.setBudgetInMillions(rs.getDouble("budget_in_millions"));
                    return team;
                })
                .optional()
                .orElse(null);
    }

    @Override
    public void add(Team team) {
        String sql = "INSERT INTO teams (name, founded_year, league, team_logo_url, budget_in_millions) " +
                     "VALUES (:name, :foundedYear, :league, :teamLogoUrl, :budget)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("name", team.getName())
                .param("foundedYear", team.getFoundedYear())
                .param("league", team.getLeague().name())
                .param("teamLogoUrl", team.getTeamLogoUrl())
                .param("budget", team.getBudgetInMillions())
                .update(keyHolder);

        team.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Team team) {
        String sql = "UPDATE teams SET name = :name, founded_year = :foundedYear, " +
                     "league = :league, team_logo_url = :teamLogoUrl, budget_in_millions = :budget " +
                     "WHERE id = :id";

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", team.getName())
                .param("foundedYear", team.getFoundedYear())
                .param("league", team.getLeague().name())
                .param("teamLogoUrl", team.getTeamLogoUrl())
                .param("budget", team.getBudgetInMillions())
                .param("id", team.getId())
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Team with id " + team.getId() + " not found");
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM teams WHERE id = :id";
        jdbcClient.sql(sql).param("id", id).update();
    }
}
