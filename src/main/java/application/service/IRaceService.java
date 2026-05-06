package application.service;

import application.domain.Race;
import application.service.command.AddRaceCommand;
import application.service.command.UpdateRaceCommand;

import java.util.List;

public interface IRaceService {

    Race getById(Integer id);

    void delete(Integer id);

    List<Race> findRacesByDriverId(Integer driverId);

    List<Race> findUpcomingRaces();

    List<Race> findByTrackId(Integer trackId);

    List<Race> getAllRaces();

    void addRace(AddRaceCommand addRaceCommand);

    Race updateRace(UpdateRaceCommand updateRaceCommand);

    void removeDriverFromRace(Integer raceId, Integer driverId);

    void addDriverToRace(Integer raceId, Integer driverId, Integer position);
}
