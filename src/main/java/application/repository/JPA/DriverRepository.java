package application.repository.JPA;

import application.domain.Driver;
import application.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("driverRepository")
@Profile("spring-data")
@Primary
public class DriverRepository implements IRepository<Driver> {

    private final IDriverRepository repository;

    @Autowired
    public DriverRepository(IDriverRepository repository) {
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

    // Method queries
    public List<Driver> findByNationality(String nationality) {
        return repository.findByNationality(nationality);
    }

    public List<Driver> findByWorldChampionshipsGreaterThan(Integer championships) {
        return repository.findByWorldChampionshipsGreaterThan(championships);
    }
}
