package application.repository.local;

import application.domain.*;
import application.repository.IRaceRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("collection")
public class RaceRepository implements IRaceRepository {

    private final List<Race> races = new ArrayList<>();

    public RaceRepository() {
        seed();
    }

    private void seed() {

        Race race1 = new Race("Monaco Grand Prix", LocalDate.of(2025, 5, 25));
        Race race2 = new Race("British Grand Prix", LocalDate.of(2025, 7, 13));

        this.add(race1);
        this.add(race2);
    }

    @Override
    public List<Race> getAll() {
        return races;
    }

    @Override
    public Race getById(int id) {
        return races.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void add(Race race) {
        race.setId(races.size() + 1);
        races.add(race);
    }

    @Override
    public void update(Race race) {
        for (int i = 0; i < races.size(); i++) {
            if (races.get(i).getId() == race.getId()) {
                races.set(i, race);
                return;
            }
        }
        throw new IllegalArgumentException("Race with id " + race.getId() + " not found");
    }

    @Override
    public void delete(int id) {
        races.removeIf(r -> r.getId() == id);
    }

    @Override
    public List<Race> findUpcomingRaces() {
        return races.stream()
                .filter(race -> !race.isHasEnded())
                .sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .toList();
    }

    @Override
    public List<Race> findRacesByDriverId(Integer driverId) {
        return races.stream()
                .filter(race -> race.getDrivers() != null &&
                        race.getDrivers().stream().anyMatch(driver -> driver.getId() == driverId))
                .toList();
    }

    @Override
    public List<Race> findByTrackId(Integer trackId) {
        return races.stream()
                .filter(race -> race.getTrack() != null && race.getTrack().getId() == trackId)
                .toList();
    }
}
