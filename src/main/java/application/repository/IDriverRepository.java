package application.repository;

import application.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, Integer> {

    @Query("SELECT DISTINCT d FROM Driver d " +
            "LEFT JOIN FETCH d.team " +
            "LEFT JOIN FETCH d.raceDrivers")
    List<Driver> findAll();

    @Query("SELECT d FROM Driver d " +
            "LEFT JOIN FETCH d.team " +
            "LEFT JOIN FETCH d.raceDrivers " +
            "WHERE d.id = :id")
    Optional<Driver> findById(@Param("id") Integer id);

    List<Driver> findAllById(Iterable<Integer> id);

    List<Driver> findByWorldChampionshipsGreaterThan(@Param("championships") Integer championships);

    @Query("SELECT DISTINCT d FROM Driver d " +
            "LEFT JOIN FETCH d.team " +
            "LEFT JOIN FETCH d.raceDrivers " +
            "WHERE d.team.id = :teamId")
    List<Driver> findByTeamId(@Param("teamId") Integer teamId);
}
