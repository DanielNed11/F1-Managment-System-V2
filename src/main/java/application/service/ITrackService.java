package application.service;

import application.domain.Track;
import java.util.List;

public interface ITrackService extends IService<Track> {
    List<Track> filterTracks(String location, Double minLength, Double maxLength);
    List<Track> findLongTracks();
}
