package application.service.impl;

import application.domain.Race;
import application.repository.IRaceRepository;
import application.service.IRaceService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    public Race getById(Integer id) {
        Race race = raceRepository.findById(id).orElse(null);
        if (race != null) {
            Hibernate.initialize(race.getTrack());
            Hibernate.initialize(race.getWinner());
            Hibernate.initialize(race.getDriverRaces());
            race.getDriverRaces().forEach(dr -> Hibernate.initialize(dr.getDriver()));
        }
        return race;
    }

    @Override
    @Transactional
    public void add(Race race) {
        raceRepository.save(race);
    }

    @Override
    @Transactional
    public void update(Race race) {
        raceRepository.save(race);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        raceRepository.deleteById(id);
    }

    @Override
    public List<Race> findRacesByDriverId(Integer driverId) {
        List<Race> races = raceRepository.findRacesByDriverId(driverId);
        races.forEach(r -> {
            Hibernate.initialize(r.getTrack());
            Hibernate.initialize(r.getWinner());
            Hibernate.initialize(r.getDriverRaces());
        });
        return races;
    }

    @Override
    public List<Race> findUpcomingRaces() {
        List<Race> races = raceRepository.findUpcomingRaces();
        races.forEach(r -> {
            Hibernate.initialize(r.getTrack());
            Hibernate.initialize(r.getWinner());
            Hibernate.initialize(r.getDriverRaces());
        });
        return races;
    }

    @Override
    public List<Race> findByTrackId(Integer trackId) {
        List<Race> races = raceRepository.findByTrackId(trackId);
        races.forEach(r -> {
            Hibernate.initialize(r.getTrack());
            Hibernate.initialize(r.getWinner());
            Hibernate.initialize(r.getDriverRaces());
        });
        return races;
    }
}
