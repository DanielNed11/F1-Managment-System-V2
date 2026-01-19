package application.repository.JPA;

import application.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackJpaRepository extends JpaRepository<Track, Integer> {

    List<Track> findByLengthKmGreaterThan(Double length);
}
