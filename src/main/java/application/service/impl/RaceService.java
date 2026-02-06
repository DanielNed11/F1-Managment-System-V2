package application.service.impl;

import application.domain.Race;
import application.repository.IRaceRepository;
import application.service.IRaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService implements IRaceService {

    private final IRaceRepository raceRepository;

    @Autowired
    public RaceService(IRaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public List<Race> getAll() {
        return raceRepository.findAll();
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
        raceRepository.delete(raceRepository.findById(id).get());
    }

    @Override
    public List<Race> findRacesByDriverId(Integer driverId) {
        return raceRepository.findRacesByDriverId(driverId);
    }

    @Override
    public List<Race> findUpcomingRaces() {
        return raceRepository.findUpcomingRaces();
    }

    @Override
    public List<Race> findByTrackId(Integer trackId) {
        return raceRepository.findByTrackId(trackId);
    }
}
