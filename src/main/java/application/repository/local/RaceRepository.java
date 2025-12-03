package application.repository.local;

import application.domain.*;
import application.repository.IRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("collection")
public class RaceRepository implements IRepository<Race> {

    private final List<Race> races = new ArrayList<>();

    public RaceRepository() {
        seed();
    }

    private void seed() {

        Race race1 = new Race("Monaco Grand Prix", LocalDate.of(2025, 5, 25), false);
        Race race2 = new Race("British Grand Prix", LocalDate.of(2025, 7, 13),  false);

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
        // Implementation needed?
    }

    @Override
    public void delete(int id) {
        races.removeIf(r -> r.getId() == id);
    }
}
