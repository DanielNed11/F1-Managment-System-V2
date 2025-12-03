package application.service.impl;

import application.domain.Race;
import application.repository.IRepository;
import application.service.IRaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService implements IRaceService {

    private final IRepository<Race> raceRepository;

    @Autowired
    public RaceService(IRepository<Race> raceRepository) {
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

    @Override
    public void delete(int id) {
        raceRepository.delete(id);
    }
}
