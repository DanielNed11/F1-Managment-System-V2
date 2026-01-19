package application.repository;

import application.domain.Race;

import java.util.List;

public interface IRaceRepository extends IRepository<Race> {

    List<Race> findUpcomingRaces();

    List<Race> findRacesByDriverId(Integer driverId);

    List<Race> findByTrackId(Integer trackId);
}
