package application.repository.JPA;

import application.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverJpaRepository extends JpaRepository<Driver, Integer> {

    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.team")
    List<Driver> findAll();

    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.team WHERE d.worldChampionships > :championships")
    List<Driver> findByWorldChampionshipsGreaterThan(@Param("championships") Integer championships);

    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.team WHERE d.team.id = :teamId")
    List<Driver> findByTeamId(@Param("teamId") Integer teamId);
}
