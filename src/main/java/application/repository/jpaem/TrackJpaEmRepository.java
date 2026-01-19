package application.repository.jpaem;

import application.domain.Track;
import application.repository.ITrackRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("trackJpaEmRepository")
@Profile("jpa")
@Primary
@Transactional
public class TrackJpaEmRepository implements ITrackRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Track> getAll() {
        TypedQuery<Track> query = entityManager.createQuery(
                "SELECT t FROM Track t", Track.class);
        return query.getResultList();
    }

    @Override
    public Track getById(int id) {
        return entityManager.find(Track.class, id);
    }

    @Override
    public void add(Track entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(Track entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Track track = entityManager.find(Track.class, id);
        if (track != null) {
            entityManager.remove(track);
        }
    }

    @Override
    public List<Track> findByLengthKmGreaterThan(Double length) {
        TypedQuery<Track> query = entityManager.createQuery(
                "SELECT t FROM Track t WHERE t.lengthKm > :length", Track.class);
        query.setParameter("length", length);
        return query.getResultList();
    }
}
