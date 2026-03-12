package application.repository;

import application.domain.RaceDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDriverRaceRepository extends JpaRepository<RaceDriver, Integer> {

    @Query("SELECT dr FROM RaceDriver dr " +
            "LEFT JOIN FETCH dr.race r " +
            "LEFT JOIN FETCH r.track " +
            "LEFT JOIN FETCH r.winner " +
            "LEFT JOIN FETCH dr.driver " +
            "WHERE dr.driver.id = :driverId")
    Optional<List<RaceDriver>> findByDriverId(@Param("driverId") Integer driverId);

    @Query("SELECT dr FROM RaceDriver dr " +
            "LEFT JOIN FETCH dr.driver " +
            "WHERE dr.race.id = :raceId")
    List<RaceDriver> findByRaceId(@Param("raceId") Integer raceId);
}
