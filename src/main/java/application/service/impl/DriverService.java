package application.service.impl;

import application.domain.*;
import application.mapper.DriverMapper;
import application.repository.IAppUserRepository;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.repository.ITeamRepository;
import application.service.IDriverService;
import application.viewmodel.AddDriverDto;
import application.viewmodel.PatchDriverDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


// TODO Throw exceptions
@Service
@Transactional
public class DriverService implements IDriverService {

    private final DriverMapper driverMapper;
    private final IDriverRepository driverRepository;
    private final IDriverRaceRepository driverRaceRepository;
    private final IAppUserRepository iAppUserRepository;
    private final ITeamRepository iTeamRepository;

    @Autowired
    public DriverService(IDriverRepository driverRepository,
                         IDriverRaceRepository driverRaceRepository,
                         DriverMapper driverMapper,
                         IAppUserRepository iAppUserRepository, ITeamRepository iTeamRepository) {
        this.driverRepository = driverRepository;
        this.driverRaceRepository = driverRaceRepository;
        this.driverMapper = driverMapper;
        this.iAppUserRepository = iAppUserRepository;
        this.iTeamRepository = iTeamRepository;
    }

    @Override
    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getById(Integer id) {
        return driverRepository.findById(id).orElse(null);
    }

    @Override
    public Driver add(AddDriverDto addDriverDto, int appUserId) {
        Driver driver = driverMapper.toDriver(addDriverDto);

        AppUser appUser = iAppUserRepository.findById(appUserId).orElseThrow(NoSuchElementException::new);
        Team team = appUser.getManagerInTeam();
        driver.setTeam(team);

        Driver saved = driverRepository.save(driver);

        return driverRepository.findById(saved.getId()).orElse(null);
    }

    @Override
    public Driver update(Integer id, PatchDriverDTO patchDriverDTO, int appUserId) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with id " + id + " not found");
        }

        if (!canModifyDriver(id, appUserId)) {
            throw new AccessDeniedException("Access denied");
        }

        driverMapper.patchDTOToDriver(patchDriverDTO, driver);

        if (patchDriverDTO.getTeamId() != null) {
            if (patchDriverDTO.getTeamId() == 0) {
                driver.setTeam(null);
            } else {
                Team team = iTeamRepository.findById(patchDriverDTO.getTeamId())
                        .orElseThrow(NoSuchElementException::new);
                driver.setTeam(team);
            }
        }

        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth) {
        return driverRepository.findAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || (d.getDateOfBirth() != null && d.getDateOfBirth().equals(dateOfBirth)))
                .toList();
    }

    @Override
    public void delete(Integer id, int appUserId) {
        if (canModifyDriver(id, appUserId)) driverRepository.deleteById(id);
        else throw new AccessDeniedException("Access denied");

    }

    @Override
    public List<Driver> findChampions() {
        return driverRepository.findByWorldChampionshipsGreaterThan(0);
    }

    @Override
    public List<Driver> findByTeamId(Integer teamId) {
        return driverRepository.findByTeamId(teamId);
    }


    @Override
    public List<Race> getRacesByDriver(Integer id) {
        return driverRaceRepository
                .findByDriverId(id)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(RaceDriver::getRace)
                .toList();
    }

    @Override
    public boolean canModifyDriver(Integer driverId, int appUserId) {
        AppUser appUser = iAppUserRepository.findById(appUserId).orElseThrow(NoSuchElementException::new);

        if (appUser.getRole() == Role.ADMIN) {
            return true;
        }

        Team driversTeam = driverRepository.findById(driverId).orElseThrow(NoSuchElementException::new).getTeam();
        Team managedTeam = appUser.getManagerInTeam();

        return managedTeam != null && driversTeam != null
                && Objects.equals(managedTeam.getId(), driversTeam.getId());
    }

}
