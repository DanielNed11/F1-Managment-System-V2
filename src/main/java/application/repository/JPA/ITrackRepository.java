package application.repository.JPA;

import application.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITrackRepository extends JpaRepository<Track, Integer> {

    // Method query: find tracks by location
    List<Track> findByLocation(String location);

    // Method query: find tracks with length between min and max
    List<Track> findByLengthKmBetween(Double minLength, Double maxLength);
}
