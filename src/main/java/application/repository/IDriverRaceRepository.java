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
    Optional<RaceDriver> findByDriverIdAndRaceId(@Param("driverId") Integer driverId, @Param("raceId") Integer raceId);

    @Query("SELECT DISTINCT dr FROM RaceDriver dr " +
            "LEFT JOIN FETCH dr.race r " +
            "LEFT JOIN FETCH r.track " +
            "LEFT JOIN FETCH r.winner " +
            "LEFT JOIN FETCH dr.driver " +
            "WHERE dr.driver.id = :driverId")
    Optional<List<RaceDriver>> findByDriverId(@Param("driverId") Integer driverId);
}
