package application.repository.JPA;

import application.domain.Race;
import application.repository.IRaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("raceRepository")
@Profile("spring-data")
@Primary
public class RaceRepository implements IRaceRepository {

    private final RaceJpaRepository repository;

    @Autowired
    public RaceRepository(RaceJpaRepository repository) {
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

    @Override
    public List<Race> findUpcomingRaces() {
        return repository.findUpcomingRaces();
    }

    @Override
    public List<Race> findRacesByDriverId(Integer driverId) {
        return repository.findRacesByDriverId(driverId);
    }

    @Override
    public List<Race> findByTrackId(Integer trackId) {
        return repository.findByTrackId(trackId);
    }
}
