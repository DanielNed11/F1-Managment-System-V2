package application.service;

import application.domain.Track;
import application.repository.DataFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    public List<Track> getTracks() {
        return DataFactory.tracks;
    }
}
