package application.repository;

import application.domain.DriverRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDriverRaceRepository extends JpaRepository<DriverRace, Integer> {
    Optional<DriverRace> findByDriverIdAndRaceId(@Param("driverId") Integer driverId, @Param("raceId") Integer raceId);
}
