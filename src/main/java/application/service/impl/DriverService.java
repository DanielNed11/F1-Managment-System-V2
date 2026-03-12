package application.service.impl;

import application.domain.Driver;
import application.domain.RaceDriver;
import application.mapper.DriverMapper;
import application.mapper.RaceMapper;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.service.IDriverService;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import application.viewmodel.RaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


// TODO Throw exceptions
@Service
@Transactional
public class DriverService implements IDriverService {

    private final DriverMapper driverMapper;
    private final IDriverRepository driverRepository;
    private final IDriverRaceRepository driverRaceRepository;
    private final RaceMapper raceMapper;

    @Autowired
    public DriverService(IDriverRepository driverRepository,
                         IDriverRaceRepository driverRaceRepository,
                         DriverMapper driverMapper, RaceMapper raceMapper) {
        this.driverRepository = driverRepository;
        this.driverRaceRepository = driverRaceRepository;
        this.driverMapper = driverMapper;
        this.raceMapper = raceMapper;
    }

    @Override
    public List<DriverDTO> getAll() {
        return driverMapper.toDriverDTOList(driverRepository.findAll());
    }

    @Override
    public DriverDTO getById(Integer id) {
        return driverMapper.toDriverDTO(driverRepository.findById(id).orElse(null));
    }

    @Override
    public void add(DriverDTO driverDTO) {
        driverRepository.save(driverMapper.toDriver(driverDTO));
    }

    public DriverDTO add(AddDriverDtoService addDriverDtoService) {
        Driver driver = new Driver();
        driver.setId(null);
        driver.setName(addDriverDtoService.name);
        driver.setDateOfBirth(addDriverDtoService.dateOfBirth);
        driver.setNationality(addDriverDtoService.nationality);
        driver.setWorldChampionships(addDriverDtoService.worldChampionships);
        driver.setImageUrl(addDriverDtoService.imageUrl);

        driverRepository.save(driver);
        return driverMapper.toDriverDTO(driver);
    }

    @Override
    public void update(DriverDTO updatedDriverDTO) {
        Driver existingDriver = driverRepository.findById(updatedDriverDTO.getId()).orElse(null);
        if (existingDriver == null) {
            throw new IllegalArgumentException("Driver with id " + updatedDriverDTO.getId() + " not found");
        }

        driverRepository.save(driverMapper.toDriver(updatedDriverDTO));
    }

    public DriverDTO update(Integer id, PatchDriverDTO patchDriverDTO) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with id " + id + " not found");
        }

        driverMapper.patchDTOToDriver(patchDriverDTO, driver);
        return driverMapper.toDriverDTO(driverRepository.save(driver));
    }

    @Override
    public List<DriverDTO> filterDrivers(String nationality, LocalDate dateOfBirth) {
        return driverRepository.findAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || (d.getDateOfBirth() != null && d.getDateOfBirth().equals(dateOfBirth)))
                .map(driverMapper::toDriverDTO)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        driverRepository.deleteById(id);
    }

    @Override
    public List<DriverDTO> findChampions() {
        return driverMapper.toDriverDTOList(driverRepository.findByWorldChampionshipsGreaterThan(0));
    }

    @Override
    public List<DriverDTO> findByTeamId(Integer teamId) {
        return driverMapper.toDriverDTOList(driverRepository.findByTeamId(teamId));
    }


    @Override
    public List<RaceDTO> getRacesByDriver(Integer id) {
        return driverRaceRepository.findByDriverId(id).get().stream().map(RaceDriver::getRace).map(raceMapper::toRaceDTO).toList();
    }

    public record AddDriverDtoService(String name, LocalDate dateOfBirth, String nationality, int worldChampionships, String imageUrl) {

    }

}

