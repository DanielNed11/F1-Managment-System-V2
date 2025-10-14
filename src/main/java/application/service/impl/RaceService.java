package application.service.impl;

import application.domain.Race;
import application.repository.RaceRepository;
import application.service.IRaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService implements IRaceService {

    private final RaceRepository raceRepository;

    @Autowired
    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public List<Race> getAll() {
        return raceRepository.getAll();
    }

    @Override
    public Race getById(int id) {
        return raceRepository.getById(id);
    }

    @Override
    public void add(Race race) {
        raceRepository.add(race);
    }

    @Override
    public void update(Race race) {
        raceRepository.update(race);
    }
}
