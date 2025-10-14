package application.repository;

import application.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
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
        for (int i = 0; i < races.size(); i++) {
            if (races.get(i).getId() == race.getId()) {
                LocalDate date = races.get(i).getDate();
                race.setDate(date);

                races.set(i, race);
                return;
            }
        }
        throw new IllegalArgumentException("Race with id " + race.getId() + " not found");
    }
}
