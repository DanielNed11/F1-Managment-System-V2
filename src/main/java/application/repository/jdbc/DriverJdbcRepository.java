package application.repository.jdbc;

import application.domain.Driver;
import application.domain.Team;
import application.repository.IDriverRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Repository("driverRepository")
@Profile("jdbc")
@Primary
public class DriverJdbcRepository implements IDriverRepository {

    private final JdbcClient jdbcClient;

    public DriverJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Driver> getAll() {
        String sql = "SELECT d.*, t.id as t_id, t.name as t_name " +
                     "FROM drivers d " +
                     "LEFT JOIN teams t ON d.team_id = t.id";
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setName(rs.getString("name"));
                    driver.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                            rs.getDate("date_of_birth").toLocalDate() : null);
                    driver.setNationality(rs.getString("nationality"));
                    driver.setWorldChampionships(rs.getInt("world_championships"));
                    driver.setImageUrl(rs.getString("image_url"));

                    int teamId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        Team team = new Team();
                        team.setId(teamId);
                        team.setName(rs.getString("t_name"));
                        driver.setTeam(team);
                    }

                    return driver;
                })
                .list();
    }

    @Override
    public Driver getById(int id) {
        String sql = "SELECT d.*, t.id as t_id, t.name as t_name " +
                     "FROM drivers d " +
                     "LEFT JOIN teams t ON d.team_id = t.id " +
                     "WHERE d.id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setName(rs.getString("name"));
                    driver.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                            rs.getDate("date_of_birth").toLocalDate() : null);
                    driver.setNationality(rs.getString("nationality"));
                    driver.setWorldChampionships(rs.getInt("world_championships"));
                    driver.setImageUrl(rs.getString("image_url"));

                    int teamId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        Team team = new Team();
                        team.setId(teamId);
                        team.setName(rs.getString("t_name"));
                        driver.setTeam(team);
                    }

                    return driver;
                })
                .optional()
                .orElse(null);
    }

    @Override
    @Transactional
    public void add(Driver driver) {
        String sql = "INSERT INTO drivers (name, date_of_birth, nationality, world_championships, image_url, team_id) " +
                     "VALUES (:name, :dateOfBirth, :nationality, :worldChampionships, :imageUrl, :teamId)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("name", driver.getName())
                .param("dateOfBirth", driver.getDateOfBirth() != null ? Date.valueOf(driver.getDateOfBirth()) : null)
                .param("nationality", driver.getNationality())
                .param("worldChampionships", driver.getWorldChampionships())
                .param("imageUrl", driver.getImageUrl())
                .param("teamId", driver.getTeam() != null ? driver.getTeam().getId() : null)
                .update(keyHolder);

        driver.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    @Transactional
    public void update(Driver driver) {
        String sql = "UPDATE drivers SET name = :name, date_of_birth = :dateOfBirth, " +
                     "nationality = :nationality, world_championships = :worldChampionships, " +
                     "image_url = :imageUrl, team_id = :teamId " +
                     "WHERE id = :id";

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", driver.getName())
                .param("dateOfBirth", driver.getDateOfBirth() != null ? Date.valueOf(driver.getDateOfBirth()) : null)
                .param("nationality", driver.getNationality())
                .param("worldChampionships", driver.getWorldChampionships())
                .param("imageUrl", driver.getImageUrl())
                .param("teamId", driver.getTeam() != null ? driver.getTeam().getId() : null)
                .param("id", driver.getId())
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Driver with id " + driver.getId() + " not found");
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        String sql = "DELETE FROM drivers WHERE id = :id";
        jdbcClient.sql(sql).param("id", id).update();
    }

    @Override
    public List<Driver> findByWorldChampionshipsGreaterThan(Integer championships) {
        String sql = "SELECT d.*, t.id as t_id, t.name as t_name " +
                     "FROM drivers d " +
                     "LEFT JOIN teams t ON d.team_id = t.id " +
                     "WHERE d.world_championships > :championships";
        return jdbcClient.sql(sql)
                .param("championships", championships)
                .query((rs, rowNum) -> {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setName(rs.getString("name"));
                    driver.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                            rs.getDate("date_of_birth").toLocalDate() : null);
                    driver.setNationality(rs.getString("nationality"));
                    driver.setWorldChampionships(rs.getInt("world_championships"));
                    driver.setImageUrl(rs.getString("image_url"));

                    int teamId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        Team team = new Team();
                        team.setId(teamId);
                        team.setName(rs.getString("t_name"));
                        driver.setTeam(team);
                    }

                    return driver;
                })
                .list();
    }

    @Override
    public List<Driver> findByTeamId(Integer teamId) {
        String sql = "SELECT d.*, t.id as t_id, t.name as t_name " +
                     "FROM drivers d " +
                     "LEFT JOIN teams t ON d.team_id = t.id " +
                     "WHERE d.team_id = :teamId";
        return jdbcClient.sql(sql)
                .param("teamId", teamId)
                .query((rs, rowNum) -> {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setName(rs.getString("name"));
                    driver.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                            rs.getDate("date_of_birth").toLocalDate() : null);
                    driver.setNationality(rs.getString("nationality"));
                    driver.setWorldChampionships(rs.getInt("world_championships"));
                    driver.setImageUrl(rs.getString("image_url"));

                    int tId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        Team team = new Team();
                        team.setId(tId);
                        team.setName(rs.getString("t_name"));
                        driver.setTeam(team);
                    }

                    return driver;
                })
                .list();
    }
}
