package application.repository.jdbc;

import application.domain.Race;
import application.repository.IRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Repository("raceRepository")
@Profile("jdbc")
@Primary
public class RaceJdbcRepository implements IRepository<Race> {

    private final JdbcClient jdbcClient;

    public RaceJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Race> getAll() {
        String sql = "SELECT r.*, t.id as t_id, t.name as t_name, d.id as w_id, d.name as w_name " +
                     "FROM races r " +
                     "LEFT JOIN tracks t ON r.track_id = t.id " +
                     "LEFT JOIN drivers d ON r.winner_id = d.id";
        List<Race> races = jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    Race race = new Race();
                    race.setId(rs.getInt("id"));
                    race.setName(rs.getString("name"));
                    race.setDate(rs.getDate("date") != null ?
                            rs.getDate("date").toLocalDate() : null);
                    race.setHasEnded(rs.getBoolean("has_ended"));

                    int trackId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        application.domain.Track track = new application.domain.Track();
                        track.setId(trackId);
                        track.setName(rs.getString("t_name"));
                        race.setTrack(track);
                    }

                    int winnerId = rs.getInt("w_id");
                    if (!rs.wasNull()) {
                        application.domain.Driver winner = new application.domain.Driver();
                        winner.setId(winnerId);
                        winner.setName(rs.getString("w_name"));
                        race.setWinner(winner);
                    }

                    return race;
                })
                .list();

        for (Race race : races) {
            race.setDrivers(getDriversForRace(race.getId()));
        }

        return races;
    }

    @Override
    public Race getById(int id) {
        String sql = "SELECT r.*, t.id as t_id, t.name as t_name, d.id as w_id, d.name as w_name " +
                     "FROM races r " +
                     "LEFT JOIN tracks t ON r.track_id = t.id " +
                     "LEFT JOIN drivers d ON r.winner_id = d.id " +
                     "WHERE r.id = :id";
        Race race = jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> {
                    Race r = new Race();
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    r.setDate(rs.getDate("date") != null ?
                            rs.getDate("date").toLocalDate() : null);
                    r.setHasEnded(rs.getBoolean("has_ended"));

                    int trackId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        application.domain.Track track = new application.domain.Track();
                        track.setId(trackId);
                        track.setName(rs.getString("t_name"));
                        r.setTrack(track);
                    }

                    int winnerId = rs.getInt("w_id");
                    if (!rs.wasNull()) {
                        application.domain.Driver winner = new application.domain.Driver();
                        winner.setId(winnerId);
                        winner.setName(rs.getString("w_name"));
                        r.setWinner(winner);
                    }
                    return r;
                })
                .optional()
                .orElse(null);

        if (race != null) {
            race.setDrivers(getDriversForRace(race.getId()));
        }

        return race;
    }

    private List<application.domain.Driver> getDriversForRace(int raceId) {
        String sql = "SELECT d.* FROM drivers d " +
                     "JOIN race_drivers rd ON d.id = rd.driver_id " +
                     "WHERE rd.race_id = :raceId";
        return jdbcClient.sql(sql)
                .param("raceId", raceId)
                .query((rs, rowNum) -> {
                    application.domain.Driver driver = new application.domain.Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setName(rs.getString("name"));
                    driver.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                            rs.getDate("date_of_birth").toLocalDate() : null);
                    driver.setNationality(rs.getString("nationality"));
                    driver.setWorldChampionships(rs.getInt("world_championships"));
                    driver.setImageUrl(rs.getString("image_url"));
                    return driver;
                })
                .list();
    }

    @Override
    public void add(Race race) {
        String sql = "INSERT INTO races (name, date, track_id, winner_id, has_ended) " +
                     "VALUES (:name, :date, :trackId, :winnerId, :hasEnded)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        Integer trackId = race.getTrack() != null ? race.getTrack().getId() : null;
        Integer winnerId = race.getWinner() != null ? race.getWinner().getId() : null;

        jdbcClient.sql(sql)
                .param("name", race.getName())
                .param("date", race.getDate() != null ? Date.valueOf(race.getDate()) : null)
                .param("trackId", trackId)
                .param("winnerId", winnerId)
                .param("hasEnded", race.isHasEnded())
                .update(keyHolder);

        race.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (race.getDrivers() != null && !race.getDrivers().isEmpty()) {
            String driverSql = "INSERT INTO race_drivers (race_id, driver_id) VALUES (:raceId, :driverId)";
            for (var driver : race.getDrivers()) {
                jdbcClient.sql(driverSql)
                        .param("raceId", race.getId())
                        .param("driverId", driver.getId())
                        .update();
            }
        }
    }

    @Override
    public void update(Race race) {
        String sql = "UPDATE races SET name = :name, date = :date, " +
                     "track_id = :trackId, winner_id = :winnerId, has_ended = :hasEnded " +
                     "WHERE id = :id";

        Integer trackId = race.getTrack() != null ? race.getTrack().getId() : null;
        Integer winnerId = race.getWinner() != null ? race.getWinner().getId() : null;

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", race.getName())
                .param("date", race.getDate() != null ? Date.valueOf(race.getDate()) : null)
                .param("trackId", trackId)
                .param("winnerId", winnerId)
                .param("hasEnded", race.isHasEnded())
                .param("id", race.getId())
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Race with id " + race.getId() + " not found");
        }

        // Update race-driver relationships
        // First, delete existing relationships
        String deleteSql = "DELETE FROM race_drivers WHERE race_id = :raceId";
        jdbcClient.sql(deleteSql)
                .param("raceId", race.getId())
                .update();

        // Then insert new relationships
        if (race.getDrivers() != null && !race.getDrivers().isEmpty()) {
            String insertSql = "INSERT INTO race_drivers (race_id, driver_id) VALUES (:raceId, :driverId)";
            for (var driver : race.getDrivers()) {
                jdbcClient.sql(insertSql)
                        .param("raceId", race.getId())
                        .param("driverId", driver.getId())
                        .update();
            }
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM races WHERE id = :id";
        jdbcClient.sql(sql).param("id", id).update();
    }
}
