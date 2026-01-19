package application.repository.JPA;

import application.domain.Track;
import application.repository.ITrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("trackRepository")
@Profile("spring-data")
@Primary
public class TrackRepository implements ITrackRepository {

    private final TrackJpaRepository repository;

    @Autowired
    public TrackRepository(TrackJpaRepository repository) {
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

    @Override
    public List<Track> findByLengthKmGreaterThan(Double length) {
        return repository.findByLengthKmGreaterThan(length);
    }
}
