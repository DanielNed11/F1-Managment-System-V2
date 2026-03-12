package application.service;

import application.domain.Track;

import java.util.List;

public interface ITrackService {

    List<Track> getAll();

    Track getById(Integer id);

    void add(Track entity);

    void update(Track entity);

    void delete(Integer id);

    List<Track> filterTracks(String location, Double minLength, Double maxLength);

    List<Track> findLongTracks();
}
