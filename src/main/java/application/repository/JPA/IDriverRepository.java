package application.repository.JPA;

import application.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, Integer> {

    // Method query: find drivers by nationality
    List<Driver> findByNationality(String nationality);

    // Method query: find drivers with more than specified number of championships
    List<Driver> findByWorldChampionshipsGreaterThan(Integer championships);
}
