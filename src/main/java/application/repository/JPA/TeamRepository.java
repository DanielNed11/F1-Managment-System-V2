package application.repository.JPA;

import application.domain.Team;
import application.repository.ITeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("teamRepository")
@Profile("spring-data")
@Primary
public class TeamRepository implements ITeamRepository {

    private final TeamJpaRepository repository;

    @Autowired
    public TeamRepository(TeamJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Team> getAll() {
        return repository.findAll();
    }

    @Override
    public Team getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void add(Team entity) {
        repository.save(entity);
    }

    @Override
    public void update(Team entity) {
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }
}
