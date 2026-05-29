package application.service.impl;

import application.domain.*;
import application.repository.IAppUserRepository;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.repository.ITeamRepository;
import application.service.IDriverService;
import application.service.command.UpdateDriverCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

// TODO Throw exceptions
@Service
@Transactional
public class DriverService implements IDriverService {

    private final IDriverRepository driverRepository;
    private final IDriverRaceRepository driverRaceRepository;
    private final IAppUserRepository iAppUserRepository;
    private final ITeamRepository iTeamRepository;

    @Autowired
    public DriverService(IDriverRepository driverRepository,
                         IDriverRaceRepository driverRaceRepository,
                         IAppUserRepository iAppUserRepository, ITeamRepository iTeamRepository) {
        this.driverRepository = driverRepository;
        this.driverRaceRepository = driverRaceRepository;
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
    @CacheEvict(value = "filterDrivers", allEntries = true)
    public Driver add(Driver driver, int appUserId) {

        AppUser appUser = iAppUserRepository.findById(appUserId)
                .orElseThrow(NoSuchElementException::new);

        Team team = appUser.getManagerInTeam();

        driver.setTeam(team);

        Driver saved = driverRepository.save(driver);

        return driverRepository.findById(saved.getId()).orElse(null);
    }

    @Override
    @CacheEvict(value = "filterDrivers", allEntries = true)
    public Driver update(UpdateDriverCommand updateDriver, int appUserId) {
        Driver existing = driverRepository.findById(updateDriver.getId()).orElse(null);

        if (existing == null) throw new NoSuchElementException("Driver not found");

        Integer id = existing.getId();

        if (!canModifyDriver(id, appUserId)) {
            throw new AccessDeniedException("Access denied");
        }

        if (updateDriver.getName() != null)
            existing.setName(updateDriver.getName());
        if (updateDriver.getDateOfBirth() != null)
            existing.setDateOfBirth(updateDriver.getDateOfBirth());
        if (updateDriver.getNationality() != null)
            existing.setNationality(updateDriver.getNationality());
        if (updateDriver.getWorldChampionships() != null)
            existing.setWorldChampionships(updateDriver.getWorldChampionships());
        if (updateDriver.getImageUrl() != null)
            existing.setImageUrl(updateDriver.getImageUrl());

        Integer teamId = updateDriver.getTeamId();

        if (teamId != null) {
            if (teamId == 0) existing.setTeam(null);
            else {
                Team updatedTeam = iTeamRepository.findById(teamId).orElseThrow(NoSuchElementException::new);
                existing.setTeam(updatedTeam);
            }
        }

        return driverRepository.save(existing);
    }

    @Override
    @Cacheable("filterDrivers")
    public List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth) {
        return driverRepository.findAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || (d.getDateOfBirth() != null && d.getDateOfBirth().equals(dateOfBirth)))
                .toList();
    }

    @Override
    @Cacheable("filterDrivers")
    public List<Driver> filterDrivers(String nationality) {
        return filterDrivers(nationality, null);
    }

    @Override
    @CacheEvict(value = "filterDrivers", allEntries = true)
    public void delete(Integer id, int appUserId) {
        if (canModifyDriver(id, appUserId)) driverRepository.deleteById(id);
        else throw new AccessDeniedException("Access denied");

    }

    @Override
    public List<Driver> findChampions() {
        return driverRepository.findByWorldChampionshipsGreaterThan(0);
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

    @Async
    @CacheEvict(value = "filterDrivers", allEntries = true)
    @Override
    public void uploadDrivers(InputStream inputStream, Integer appUserId) {
        final Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            importLine(line, appUserId);
        }

    }

    private void importLine(final String line, Integer appUserId) {
        if (line.trim().isEmpty()) {
            return;
        }
        final String[] parts = line.split(";");
        if (parts.length != 5) {
            return;
        }
        Driver driver = new Driver();
        driver.setName(parts[0]);
        driver.setNationality(parts[1]);
        driver.setWorldChampionships(Integer.parseInt(parts[2]));
        driver.setDateOfBirth(LocalDate.parse(parts[3]));
        driver.setImageUrl(parts[4]);

        add(driver, appUserId);
    }

}
