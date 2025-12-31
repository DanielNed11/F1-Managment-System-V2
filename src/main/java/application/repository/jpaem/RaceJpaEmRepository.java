package application.repository.jpaem;

import application.domain.Race;
import application.repository.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("raceJpaEmRepository")
@Profile("jpa")
@Primary
@Transactional
public class RaceJpaEmRepository implements IRepository<Race> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Race> getAll() {
        TypedQuery<Race> query = entityManager.createQuery(
                "SELECT DISTINCT r FROM Race r " +
                "LEFT JOIN FETCH r.track " +
                "LEFT JOIN FETCH r.winner", Race.class);
        return query.getResultList();
    }

    @Override
    public Race getById(int id) {
        TypedQuery<Race> query = entityManager.createQuery(
                "SELECT r FROM Race r " +
                "LEFT JOIN FETCH r.drivers " +
                "LEFT JOIN FETCH r.track " +
                "LEFT JOIN FETCH r.winner " +
                "WHERE r.id = :id", Race.class);
        query.setParameter("id", id);
        List<Race> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void add(Race entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(Race entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Race race = entityManager.find(Race.class, id);
        if (race != null) {
            entityManager.remove(race);
        }
    }
}
