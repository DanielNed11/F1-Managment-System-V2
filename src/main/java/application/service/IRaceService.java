package application.service;

import application.domain.Race;

import java.util.List;

public interface IRaceService extends IService<Race> {
    List<Race> findRacesByDriverId(Integer driverId);

    List<Race> findUpcomingRaces();

    List<Race> findByTrackId(Integer trackId);
}
