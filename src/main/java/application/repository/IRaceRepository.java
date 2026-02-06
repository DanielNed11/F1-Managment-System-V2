package application.repository;

import application.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRaceRepository extends JpaRepository<Race, Integer> {

    @Query("SELECT DISTINCT r FROM Race r " +
           "LEFT JOIN FETCH r.track " +
           "LEFT JOIN FETCH r.winner " +
           "LEFT JOIN FETCH r.drivers")
    List<Race> findAll();

    @Query("SELECT r FROM Race r " +
           "LEFT JOIN FETCH r.track " +
           "LEFT JOIN FETCH r.winner " +
           "LEFT JOIN FETCH r.drivers " +
           "WHERE r.id = :id")
    Optional<Race> findById(@Param("id") Integer id);

    @Query("SELECT DISTINCT r FROM Race r " +
           "LEFT JOIN FETCH r.track " +
           "LEFT JOIN FETCH r.winner " +
           "LEFT JOIN FETCH r.drivers " +
           "WHERE r.hasEnded = false " +
           "AND r.date >= CURRENT_DATE " +
           "ORDER BY r.date")
    List<Race> findUpcomingRaces();

    @Query("SELECT DISTINCT r FROM Race r " +
           "LEFT JOIN FETCH r.track " +
           "LEFT JOIN FETCH r.winner " +
           "JOIN FETCH r.drivers d " +
           "WHERE d.id = :driverId")
    List<Race> findRacesByDriverId(@Param("driverId") Integer driverId);

    @Query("SELECT DISTINCT r FROM Race r " +
           "LEFT JOIN FETCH r.track " +
           "LEFT JOIN FETCH r.winner " +
           "LEFT JOIN FETCH r.drivers " +
           "WHERE r.track.id = :trackId")
    List<Race> findByTrackId(@Param("trackId") Integer trackId);

    void update(Race race);

    void delete(Race race);

    void add(Race race);
}
