package application.repository.jdbc;

import application.domain.Track;
import application.repository.ITrackRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository("trackRepository")
@Profile("jdbc")
@Primary
public class TrackJdbcRepository implements ITrackRepository {

    private final JdbcClient jdbcClient;

    public TrackJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Track> getAll() {
        String sql = "SELECT * FROM tracks";
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    Track track = new Track();
                    track.setId(rs.getInt("id"));
                    track.setName(rs.getString("name"));
                    track.setLocation(rs.getString("location"));
                    track.setLengthKm(rs.getDouble("length_km"));
                    track.setOpenedYear(rs.getInt("opened_year"));
                    return track;
                })
                .list();
    }

    @Override
    public Track getById(int id) {
        String sql = "SELECT * FROM tracks WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> {
                    Track track = new Track();
                    track.setId(rs.getInt("id"));
                    track.setName(rs.getString("name"));
                    track.setLocation(rs.getString("location"));
                    track.setLengthKm(rs.getDouble("length_km"));
                    track.setOpenedYear(rs.getInt("opened_year"));
                    return track;
                })
                .optional()
                .orElse(null);
    }

    @Override
    @Transactional
    public void add(Track track) {
        String sql = "INSERT INTO tracks (name, location, length_km, opened_year) " +
                     "VALUES (:name, :location, :lengthKm, :openedYear)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("name", track.getName())
                .param("location", track.getLocation())
                .param("lengthKm", track.getLengthKm())
                .param("openedYear", track.getOpenedYear())
                .update(keyHolder);

        track.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    @Transactional
    public void update(Track track) {
        String sql = "UPDATE tracks SET name = :name, location = :location, " +
                     "length_km = :lengthKm, opened_year = :openedYear " +
                     "WHERE id = :id";

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", track.getName())
                .param("location", track.getLocation())
                .param("lengthKm", track.getLengthKm())
                .param("openedYear", track.getOpenedYear())
                .param("id", track.getId())
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Track with id " + track.getId() + " not found");
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        String sql = "DELETE FROM tracks WHERE id = :id";
        jdbcClient.sql(sql).param("id", id).update();
    }

    @Override
    public List<Track> findByLengthKmGreaterThan(Double length) {
        String sql = "SELECT * FROM tracks WHERE length_km > :length";
        return jdbcClient.sql(sql)
                .param("length", length)
                .query((rs, rowNum) -> {
                    Track track = new Track();
                    track.setId(rs.getInt("id"));
                    track.setName(rs.getString("name"));
                    track.setLocation(rs.getString("location"));
                    track.setLengthKm(rs.getDouble("length_km"));
                    track.setOpenedYear(rs.getInt("opened_year"));
                    return track;
                })
                .list();
    }
}
