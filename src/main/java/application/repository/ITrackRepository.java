package application.repository;

import application.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITrackRepository extends JpaRepository<Track, Integer> {

    void add(Track track);

    void update(Track track);

    void delete(Track track);

    List<Track> findByLengthKmGreaterThan(@Param("lengthKm") Double length);
}
