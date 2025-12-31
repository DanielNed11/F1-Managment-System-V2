package application.repository.jpaem;

import application.domain.Team;
import application.repository.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("teamJpaEmRepository")
@Profile("jpa")
@Primary
@Transactional
public class TeamJpaEmRepository implements IRepository<Team> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Team> getAll() {
        TypedQuery<Team> query = entityManager.createQuery(
                "SELECT t FROM Team t", Team.class);
        return query.getResultList();
    }

    @Override
    public Team getById(int id) {
        return entityManager.find(Team.class, id);
    }

    @Override
    public void add(Team entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(Team entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Team team = entityManager.find(Team.class, id);
        if (team != null) {
            entityManager.remove(team);
        }
    }
}
