package application.repository.JPA;

import application.domain.Driver;
import application.repository.IDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("driverRepository")
@Profile("spring-data")
@Primary
public class DriverRepository implements IDriverRepository {

    private final DriverJpaRepository repository;

    @Autowired
    public DriverRepository(DriverJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Driver> getAll() {
        return repository.findAll();
    }

    @Override
    public Driver getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void add(Driver entity) {
        repository.save(entity);
    }

    @Override
    public void update(Driver entity) {
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<Driver> findByWorldChampionshipsGreaterThan(Integer championships) {
        return repository.findByWorldChampionshipsGreaterThan(championships);
    }

    @Override
    public List<Driver> findByTeamId(Integer teamId) {
        return repository.findByTeamId(teamId);
    }
}
