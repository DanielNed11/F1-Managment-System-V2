package application.repository;

import application.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT DISTINCT t FROM Team t " +
            "LEFT JOIN FETCH t.drivers")
    List<Team> findAllWithDrivers();

    @Query("SELECT t FROM Team t " +
            "LEFT JOIN FETCH t.drivers " +
            "WHERE t.id = :id")
    Optional<Team> findByIdWithDrivers(@Param("id") Integer id);



}
