package application.repository.JPA;

import application.domain.Track;
import application.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("trackRepository")
@Profile("spring-data")
@Primary
public class TrackRepository implements IRepository<Track> {

    private final ITrackRepository repository;

    @Autowired
    public TrackRepository(ITrackRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Track> getAll() {
        return repository.findAll();
    }

    @Override
    public Track getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void add(Track entity) {
        repository.save(entity);
    }

    @Override
    public void update(Track entity) {
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    // Method queries
    public List<Track> findByLocation(String location) {
        return repository.findByLocation(location);
    }

    public List<Track> findByLengthKmBetween(Double minLength, Double maxLength) {
        return repository.findByLengthKmBetween(minLength, maxLength);
    }
}
