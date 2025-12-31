package application.repository.JPA;

import application.domain.Race;
import application.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("raceRepository")
@Profile("spring-data")
@Primary
public class RaceRepository implements IRepository<Race> {

    private final IRaceRepository repository;

    @Autowired
    public RaceRepository(IRaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Race> getAll() {
        return repository.findAll();
    }

    @Override
    public Race getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void add(Race entity) {
        repository.save(entity);
    }

    @Override
    public void update(Race entity) {
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    // Custom queries
    public List<Race> findUpcomingRaces() {
        return repository.findUpcomingRaces();
    }

    public List<Race> findRacesByDriverId(Integer driverId) {
        return repository.findRacesByDriverId(driverId);
    }
}
