package application.service;

import application.domain.Driver;
import application.domain.Race;
import application.viewmodel.RaceViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IRaceService extends IService<Race> {
    List<Race> findRacesByDriverId(Integer driverId);

    List<Race> findUpcomingRaces();

    List<Race> findByTrackId(Integer trackId);

    void addDriverToRace(int position, Race race, Driver driver);

    void updateRace(Race updatedRace, Integer[] participatingDriverIds, Integer winnerId, Race existingRace, Map<Integer, Integer> driverPositions);

    void addRace(String name, LocalDate date, Integer trackId, Integer[] participatingDriverIds, Integer winnerId);

    RaceViewModel mapToViewModel(Race race);
}
