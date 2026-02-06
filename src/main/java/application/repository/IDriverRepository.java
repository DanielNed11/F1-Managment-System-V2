package application.repository;

import application.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, Integer> {

    List<Driver> findAll();

    List<Driver> findByWorldChampionshipsGreaterThan(@Param("championships") Integer championships);

    List<Driver> findByTeamId(@Param("teamId") Integer teamId);

    void add(Driver driver);

    void updateDriver(Driver driver);

    void delete(Driver driver);

    Driver findDriverById(@Param("id") Integer id);
}
