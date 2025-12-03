package application.service.impl;

import application.domain.Track;
import application.repository.IRepository;
import application.service.ITrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService implements ITrackService {

    private final IRepository<Track> trackRepository;

    @Autowired
    public TrackService(IRepository<Track> trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public List<Track> getAll() {
        return trackRepository.getAll();
    }

    @Override
    public Track getById(int id) {
        return trackRepository.getById(id);
    }

    @Override
    public void add(Track track) {
        trackRepository.add(track);
    }

    @Override
    public void update(Track track) {
        trackRepository.update(track);
    }

    @Override
    public List<Track> filterTracks(String location, Double minLength, Double maxLength) {
        return trackRepository.getAll().stream()
                .filter(t -> location == null || location.isEmpty() || t.getLocation().equalsIgnoreCase(location))
                .filter(t -> (minLength == null || t.getLengthKm() >= minLength) &&
                        (maxLength == null || t.getLengthKm() <= maxLength))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int id) {
        trackRepository.delete(id);
    }
}
