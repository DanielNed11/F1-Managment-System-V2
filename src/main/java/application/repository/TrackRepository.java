package application.repository;

import application.domain.Track;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrackRepository implements IRepository<Track> {
    private static final List<Track> tracks = new ArrayList<>();

    public TrackRepository() {
        this.add(new Track( "Monaco GP", "Monaco", 3.337, 1929));
        this.add(new Track( "Silverstone", "UK", 5.891, 1948));
        this.add(new Track( "Spa-Francorchamps", "Belgium", 7.004, 1921));
        this.add(new Track( "Monza", "Italy", 5.793, 1922));
        this.add(new Track( "Suzuka", "Japan", 5.807, 1962));
    }

    @Override
    public List<Track> getAll() {
        return tracks;
    }

    @Override
    public Track getById(int id) {
        return tracks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(Track track) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId() == track.getId()) {
                tracks.set(i, track);
                return;
            }
        }
        throw new IllegalArgumentException("Track with id " + track.getId() + " not found");

    }

    @Override
    public void add(Track track) {
        track.setId(tracks.size() + 1);
        tracks.add(track);
    }
}
