package application.repository;

import application.domain.Track;

import java.util.List;

public interface ITrackRepository extends IRepository<Track> {

    List<Track> findByLengthKmGreaterThan(Double length);
}
