package application.service.impl;

import application.domain.Driver;
import application.domain.Race;
import application.domain.RaceDriver;
import application.domain.Track;
import application.repository.IDriverRepository;
import application.repository.IRaceRepository;
import application.repository.ITrackRepository;
import application.service.IRaceService;
import application.service.command.AddRaceCommand;
import application.service.command.RaceDriverSelectionCommand;
import application.service.command.UpdateRaceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RaceService implements IRaceService {

    private final IRaceRepository raceRepository;
    private final ITrackRepository trackRepository;
    private final IDriverRepository driverRepository;

    @Autowired
    public RaceService(IRaceRepository raceRepository,
                       ITrackRepository trackRepository,
                       IDriverRepository driverRepository) {
        this.raceRepository = raceRepository;
        this.trackRepository = trackRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Race getById(Integer id) {
        return raceRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void delete(Integer id) {
        raceRepository.deleteById(id);
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

    @Override
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @Override
    public void addRace(AddRaceCommand addRaceCommand) {
        Race race = new Race();

        race.setName(addRaceCommand.getName());
        race.setDate(addRaceCommand.getDate());
        race.setWinner(resolveDriver(addRaceCommand.getWinnerId()));
        race.setTrack(resolveTrack(addRaceCommand.getTrackId()));
        applySelection(race, addRaceCommand.getDriverSelections());

        raceRepository.save(race);
    }

    @Override
    public Race updateRace(UpdateRaceCommand updateRaceCommand) {

        Race race = raceRepository.findById(updateRaceCommand.getId()).orElseThrow(NoSuchElementException::new);

        race.setName(updateRaceCommand.getName());
        race.setDate(updateRaceCommand.getDate());
        race.setTrack(resolveTrack(updateRaceCommand.getTrackId()));
        race.setWinner(resolveDriver(updateRaceCommand.getWinnerId()));
        applySelection(race, updateRaceCommand.getDriverSelections());

        return raceRepository.save(race);
    }

    @Override
    public void removeDriverFromRace(Integer raceId, Integer driverId) {
        Race race = raceRepository.findById(raceId).orElse(null);
        if (race == null) {
            return;
        }

        race.getRaceDrivers().removeIf(rd -> rd.getDriver() != null && rd.getDriver().getId().equals(driverId));
        raceRepository.save(race);
    }

    @Override
    public void addDriverToRace(Integer raceId, Integer driverId, Integer position) {
        Race race = raceRepository.findById(raceId).orElse(null);
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (race == null || driver == null) {
            return;
        }

        boolean exists = race.getRaceDrivers().stream()
                .anyMatch(rd -> rd.getDriver() != null && rd.getDriver().getId().equals(driverId));
        if (!exists) {
            race.addRaceDriver(new RaceDriver(driver, race, position));
            raceRepository.save(race);
        }
    }

    private Track resolveTrack(Integer trackId) {
        if (trackId == null) {
            return null;
        }
        return trackRepository.findById(trackId).orElse(null);
    }

    private void applySelection(Race race, List<RaceDriverSelectionCommand> driverSelections) {
        List<RaceDriverSelectionCommand> selected = driverSelections.stream()
                .filter(RaceDriverSelectionCommand::isParticipated)
                .filter(selection -> selection.getDriverId() != null)
                .toList();

        List<Integer> driverIds = selected.stream()
                .map(RaceDriverSelectionCommand::getDriverId)
                .toList();

        Map<Integer, Driver> driversById = driverRepository.findAllById(driverIds).stream()
                .collect(Collectors.toMap(Driver::getId, driver -> driver));

        race.getRaceDrivers().clear();

        selected.forEach(selection -> {
            Driver driver = driversById.get(selection.getDriverId());
            if (driver == null) {
                return;
            }
            RaceDriver raceDriver = new RaceDriver();
            raceDriver.setDriver(driver);
            raceDriver.setPosition(selection.getPosition());
            race.addRaceDriver(raceDriver);
        });
    }

    private Driver resolveDriver(Integer driverId) {
        if (driverId == null) {
            return null;
        }
        return driverRepository.findById(driverId).orElse(null);
    }

}
