package application.service.impl;

import application.domain.Track;
import application.repository.ITrackRepository;
import application.service.ITrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService implements ITrackService {

    private final ITrackRepository trackRepository;

    @Autowired
    public TrackService(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    @Override
    public Track getById(Integer id) {
        return trackRepository.findById(id).orElse(null);
    }

    @Override
    public void add(Track track) {
        trackRepository.save(track);
    }

    @Override
    public void update(Track track) {
        trackRepository.save(track);
    }

    @Override
    public List<Track> filterTracks(String location, Double minLength, Double maxLength) {
        return trackRepository.findAll().stream()
                .filter(t -> location == null || location.isEmpty() || t.getLocation().equalsIgnoreCase(location))
                .filter(t -> (minLength == null || t.getLengthKm() >= minLength) &&
                        (maxLength == null || t.getLengthKm() <= maxLength))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        trackRepository.deleteById(id);
    }

    @Override
    public List<Track> findLongTracks() {
        return trackRepository.findByLengthKmGreaterThan(5.0);
    }
}
