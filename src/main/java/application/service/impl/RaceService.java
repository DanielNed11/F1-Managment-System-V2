package application.service.impl;

import application.domain.Driver;
import application.domain.Race;
import application.domain.RaceDriver;
import application.domain.Track;
import application.mapper.RaceMapper;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.repository.IRaceRepository;
import application.repository.ITrackRepository;
import application.service.IRaceService;
import application.viewmodel.AddRaceDTO;
import application.viewmodel.EditRaceDTO;
import application.viewmodel.RaceDTO;
import application.viewmodel.RaceDriverSelectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class RaceService implements IRaceService {

    private final IRaceRepository raceRepository;
    private final IDriverRaceRepository driverRaceRepository;
    private final ITrackRepository trackRepository;
    private final IDriverRepository driverRepository;
    private final RaceMapper raceMapper;

    @Autowired
    public RaceService(IRaceRepository raceRepository,
                       IDriverRaceRepository driverRaceRepository,
                       ITrackRepository trackRepository,
                       IDriverRepository driverRepository,
                       RaceMapper raceMapper) {
        this.raceRepository = raceRepository;
        this.driverRaceRepository = driverRaceRepository;
        this.trackRepository = trackRepository;
        this.driverRepository = driverRepository;
        this.raceMapper = raceMapper;
    }

    @Override
    public RaceDTO getById(Integer id) {
        return raceMapper.toRaceDTO(raceRepository.findById(id).orElse(null));
    }

    @Override
    public void update(RaceDTO raceDTO) {
        raceRepository.save(raceMapper.toRace(raceDTO));
    }

    @Override
    public void delete(Integer id) {
        raceRepository.deleteById(id);
    }

    @Override
    public List<RaceDTO> findRacesByDriverId(Integer driverId) {
        return raceMapper.toRaceDTOList(raceRepository.findRacesByDriverId(driverId));
    }

    @Override
    public List<RaceDTO> findUpcomingRaces() {
        return raceMapper.toRaceDTOList(raceRepository.findUpcomingRaces());
    }

    @Override
    public List<RaceDTO> findByTrackId(Integer trackId) {
        return raceMapper.toRaceDTOList(raceRepository.findByTrackId(trackId));
    }

    @Override
    public List<RaceDTO> getAllRaces() {
        return raceMapper.toRaceDTOList(raceRepository.findAll());
    }

    @Override
    public AddRaceDTO getAddRaceForm() {
        AddRaceDTO addRaceDTO = new AddRaceDTO();
        List<RaceDriverSelectionDTO> selections = driverRepository.findAll().stream()
                .map(driver -> {
                    RaceDriverSelectionDTO selection = new RaceDriverSelectionDTO();
                    selection.setDriverId(driver.getId());
                    selection.setDriverName(driver.getName());
                    return selection;
                })
                .toList();
        addRaceDTO.setDriverSelections(selections);
        return addRaceDTO;
    }

    @Override
    public EditRaceDTO getEditRaceForm(Integer id) {
        Race race = raceRepository.findById(id).orElse(null);
        if (race == null) {
            return null;
        }

        Map<Integer, Integer> positionByDriverId = new HashMap<>();
        for (RaceDriver raceDriver : driverRaceRepository.findByRaceId(id)) {
            if (raceDriver.getDriver() != null) {
                positionByDriverId.put(raceDriver.getDriver().getId(), raceDriver.getPosition());
            }
        }

        EditRaceDTO editRaceDTO = new EditRaceDTO();
        editRaceDTO.setId(race.getId());
        editRaceDTO.setName(race.getName());
        editRaceDTO.setDate(race.getDate());
        editRaceDTO.setTrackId(race.getTrack() != null ? race.getTrack().getId() : null);
        editRaceDTO.setWinnerId(race.getWinner() != null ? race.getWinner().getId() : null);

        List<RaceDriverSelectionDTO> selections = driverRepository.findAll().stream()
                .map(driver -> {
                    RaceDriverSelectionDTO selection = new RaceDriverSelectionDTO();
                    selection.setDriverId(driver.getId());
                    selection.setDriverName(driver.getName());
                    selection.setParticipated(positionByDriverId.containsKey(driver.getId()));
                    selection.setPosition(positionByDriverId.get(driver.getId()));
                    return selection;
                })
                .toList();
        editRaceDTO.setDriverSelections(selections);
        return editRaceDTO;
    }

    @Override
    public void addRace(AddRaceDTO addRaceDTO) {
        Race race = new Race();
        race.setName(addRaceDTO.getName());
        race.setDate(addRaceDTO.getDate());
        race.setTrack(resolveTrack(addRaceDTO.getTrackId()));
        race.setWinner(resolveWinner(addRaceDTO.getWinnerId()));
        applyDriverSelections(race, addRaceDTO.getDriverSelections());
        raceRepository.save(race);
    }

    @Override
    public void updateRace(EditRaceDTO editRaceDTO) {
        Race race = raceRepository.findById(editRaceDTO.getId()).orElse(null);
        if (race == null) {
            return;
        }

        race.setName(editRaceDTO.getName());
        race.setDate(editRaceDTO.getDate());
        race.setTrack(resolveTrack(editRaceDTO.getTrackId()));
        race.setWinner(resolveWinner(editRaceDTO.getWinnerId()));
        race.getRaceDrivers().clear();
        applyDriverSelections(race, editRaceDTO.getDriverSelections());
        raceRepository.save(race);
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

    private Driver resolveWinner(Integer winnerId) {
        if (winnerId == null) {
            return null;
        }
        return driverRepository.findById(winnerId).orElse(null);
    }

    private void applyDriverSelections(Race race, List<RaceDriverSelectionDTO> selections) {
        if (selections == null || selections.isEmpty()) {
            return;
        }

        Set<Integer> selectedDriverIds = selections.stream()
                .filter(RaceDriverSelectionDTO::isParticipated)
                .map(RaceDriverSelectionDTO::getDriverId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        if (selectedDriverIds.isEmpty()) {
            return;
        }

        Map<Integer, Driver> driversById = driverRepository.findAllById(selectedDriverIds).stream()
                .collect(Collectors.toMap(Driver::getId, Function.identity()));

        for (RaceDriverSelectionDTO selection : selections) {
            if (!selection.isParticipated() || selection.getDriverId() == null) {
                continue;
            }
            Driver driver = driversById.get(selection.getDriverId());
            if (driver == null) {
                continue;
            }
            race.addRaceDriver(new RaceDriver(driver, race, selection.getPosition()));
        }
    }
}
