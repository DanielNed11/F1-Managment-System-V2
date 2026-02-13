package application.service.impl;

import application.domain.Driver;
import application.domain.RaceDriver;
import application.domain.Race;
import application.domain.Track;
import application.repository.IRaceRepository;
import application.service.IRaceService;
import application.viewmodel.RaceViewModel;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RaceService implements IRaceService {

    private final IRaceRepository raceRepository;
    private final TrackService trackService;
    private final DriverService driverService;

    @Autowired
    public RaceService(IRaceRepository raceRepository,
                       TrackService trackService,
                       DriverService driverService) {
        this.raceRepository = raceRepository;
        this.trackService = trackService;
        this.driverService = driverService;
    }

    @Override
    public List<Race> getAll() {
        return raceRepository.findAll();
    }

    @Override
    public Race getById(Integer id) {
        return raceRepository.findById(id).orElse(null);
    }

    @Override
    public void add(Race race) {
        raceRepository.save(race);
    }

    @Override
    public void update(Race race) {
        raceRepository.save(race);
    }

    @Override
    public void delete(Integer id) {
        raceRepository.deleteById(id);
    }

    @Override
    public List<Race> findRacesByDriverId(Integer driverId) {
        List<Race> races = raceRepository.findRacesByDriverId(driverId);
        races.forEach(r -> {
            Hibernate.initialize(r.getTrack());
            Hibernate.initialize(r.getWinner());
            Hibernate.initialize(r.getRaceDrivers());
        });
        return races;
    }

    @Override
    public List<Race> findUpcomingRaces() {
        List<Race> races = raceRepository.findUpcomingRaces();
        races.forEach(r -> {
            Hibernate.initialize(r.getTrack());
            Hibernate.initialize(r.getWinner());
            Hibernate.initialize(r.getRaceDrivers());
        });
        return races;
    }

    @Override
    public List<Race> findByTrackId(Integer trackId) {

        return raceRepository.findByTrackId(trackId);
    }

    @Override
    public void addDriverToRace(int position, Race race, Driver driver) {
        boolean driverExists = race.getRaceDrivers().stream()
                .anyMatch(rd -> rd.getDriver().equals(driver));

        if (!driverExists) {
            race.addDriver(driver, position);
        }

        update(race);
    }

    @Override
    public void updateRace(Race updatedRace, Integer[] participatingDriverIds, Integer winnerId, Race existingRace, Map<Integer, Integer> driverPositions) {
        if (updatedRace.getTrack() != null && updatedRace.getTrack().getId() != 0) {
            existingRace.setTrack(trackService.getById(updatedRace.getTrack().getId()));
        } else {
            existingRace.setTrack(null);
        }

        if (winnerId != null && winnerId > 0) {
            existingRace.setWinner(driverService.getById(winnerId));
        } else {
            existingRace.setWinner(null);
        }

        existingRace.getRaceDrivers().clear();

        if (participatingDriverIds != null) {
            for (Integer driverId : participatingDriverIds) {
                Driver driver = driverService.getById(driverId);
                if (driver != null) {
                    Integer position = driverPositions != null ? driverPositions.get(driverId) : null;
                    RaceDriver raceDrivers = new RaceDriver(driver, existingRace, position);
                    existingRace.addRaceDriver(raceDrivers);
                }
            }
        }

        update(existingRace);
    }

    @Override
    public void addRace(String name, LocalDate date, Integer trackId, Integer[] participatingDriverIds, Integer winnerId) {
            Race race = new Race();
            race.setName(name);
            race.setDate(date);
            race.setTrack(trackService.getById(trackId));

            if (winnerId != null && winnerId > 0) {
                race.setWinner(driverService.getById(winnerId));
            }

            // Add participating drivers with null positions (upcoming race)
            if (participatingDriverIds != null) {
                for (Integer driverId : participatingDriverIds) {
                    Driver driver = driverService.getById(driverId);
                    if (driver != null) {
                        RaceDriver raceDrivers = new RaceDriver(driver, race);  // position = null for new races
                        race.addRaceDriver(raceDrivers);
                    }
                }
            }

            add(race);

    }

    @Override
    public RaceViewModel mapToViewModel(Race race) {
        RaceViewModel viewModel = new RaceViewModel();
        viewModel.setId(race.getId());
        viewModel.setName(race.getName());
        viewModel.setDate(race.getDate());
        viewModel.setHasEnded(race.isHasEnded());

        Track track = race.getTrack();
        if (track != null) {
            viewModel.setTrackId(track.getId());
            viewModel.setTrackName(track.getName());
        }

        Driver winner = race.getWinner();
        if (winner != null) {
            viewModel.setWinnerId(winner.getId());
            viewModel.setWinnerName(winner.getName());
        }

        return viewModel;
    }
}
