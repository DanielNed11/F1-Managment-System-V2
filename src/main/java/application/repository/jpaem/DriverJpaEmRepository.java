package application.repository.jpaem;

import application.domain.Driver;
import application.repository.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("driverJpaEmRepository")
@Profile("jpa")
@Primary
@Transactional
public class DriverJpaEmRepository implements IRepository<Driver> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Driver> getAll() {
        TypedQuery<Driver> query = entityManager.createQuery(
                "SELECT d FROM Driver d LEFT JOIN FETCH d.team", Driver.class);
        return query.getResultList();
    }

    @Override
    public Driver getById(int id) {
        return entityManager.find(Driver.class, id);
    }

    @Override
    public void add(Driver entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(Driver entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Driver driver = entityManager.find(Driver.class, id);
        if (driver != null) {
            entityManager.remove(driver);
        }
    }
}
