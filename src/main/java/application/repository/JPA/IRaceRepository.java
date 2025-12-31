package application.repository.JPA;

import application.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRaceRepository extends JpaRepository<Race, Integer> {

    // Custom query: find upcoming races (not ended) ordered by date
    @Query("SELECT r FROM Race r WHERE r.hasEnded = false ORDER BY r.date")
    List<Race> findUpcomingRaces();

    // Custom query with parameter: find races by driver ID
    @Query("SELECT r FROM Race r JOIN r.drivers d WHERE d.id = :driverId")
    List<Race> findRacesByDriverId(@Param("driverId") Integer driverId);
}
