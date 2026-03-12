package application.service;

import application.viewmodel.AddRaceDTO;
import application.viewmodel.EditRaceDTO;
import application.viewmodel.RaceDTO;

import java.util.List;

public interface IRaceService {

    RaceDTO getById(Integer id);

    void update(RaceDTO entity);

    void delete(Integer id);

    List<RaceDTO> findRacesByDriverId(Integer driverId);

    List<RaceDTO> findUpcomingRaces();

    List<RaceDTO> findByTrackId(Integer trackId);

    List<RaceDTO> getAllRaces();

    AddRaceDTO getAddRaceForm();

    EditRaceDTO getEditRaceForm(Integer id);

    void addRace(AddRaceDTO addRaceDTO);

    void updateRace(EditRaceDTO editRaceDTO);

    void removeDriverFromRace(Integer raceId, Integer driverId);

    void addDriverToRace(Integer raceId, Integer driverId, Integer position);
}
