package application.service.impl;

import application.domain.Driver;
import application.domain.Race;
import application.domain.RaceDriver;
import application.domain.Team;
import application.domain.Track;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.service.IDriverService;
import application.viewmodel.DriverViewModel;
import application.viewmodel.RaceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DriverService implements IDriverService {

    private final IDriverRepository driverRepository;
    private final TeamService teamService;
    private final IDriverRaceRepository driverRaceRepository;

    @Autowired
    public DriverService(IDriverRepository driverRepository,
                         TeamService teamService,
                         IDriverRaceRepository driverRaceRepository) {
        this.driverRepository = driverRepository;
        this.teamService = teamService;
        this.driverRaceRepository = driverRaceRepository;
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
    public void add(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public void update(Driver updatedDriver) {
        Driver existingDriver = driverRepository.findById(updatedDriver.getId()).orElse(null);
        if (existingDriver == null) {
            throw new IllegalArgumentException("Driver with id " + updatedDriver.getId() + " not found");
        }

        driverRepository.save(updatedDriver);
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
    public void validateDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }

        if (driver.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Driver date of birth is required");
        }

        int age = Period.between(driver.getDateOfBirth(), LocalDate.now()).getYears();

        if (age < 18) {
            throw new IllegalArgumentException(
                    "Driver must be at least 18 years old. Current age: " + age + " years");
        }

        if (age > 60) {
            throw new IllegalArgumentException(
                    "Driver age exceeds maximum racing age of 60. Current age: " + age + " years");
        }

        if (driver.getWorldChampionships() < 0) {
            throw new IllegalArgumentException("World championships cannot be negative");
        }
    }

    @Override
    public void delete(Integer id) {
        driverRepository.deleteById(id);
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
    public DriverViewModel mapToViewModel(Driver driver) {
        DriverViewModel viewModel = new DriverViewModel();
        viewModel.setId(driver.getId());
        viewModel.setName(driver.getName());
        viewModel.setDateOfBirth(driver.getDateOfBirth());
        viewModel.setNationality(driver.getNationality());
        viewModel.setWorldChampionships(driver.getWorldChampionships());

        Team team = driver.getTeam();
        if (team != null) {
            viewModel.setTeamId(team.getId());
            viewModel.setTeamName(team.getName());
        } else {
            viewModel.setTeamId(0);
            viewModel.setTeamName(null);
        }

        viewModel.setImageUrl(driver.getImageUrl());
        return viewModel;
    }

    @Override
    public Driver mapToDriver(DriverViewModel viewModel) {
        Driver driver = new Driver();
        driver.setId(viewModel.getId());
        driver.setName(viewModel.getName());
        driver.setDateOfBirth(viewModel.getDateOfBirth());
        driver.setNationality(viewModel.getNationality());
        driver.setWorldChampionships(viewModel.getWorldChampionships());

        if (viewModel.getTeamId() != 0) {
            Team team = teamService.getById(viewModel.getTeamId());
            driver.setTeam(team);
        }

        driver.setImageUrl(viewModel.getImageUrl());
        return driver;
    }

    @Override
    public List<Race> getRaces(Integer id) {
        return driverRaceRepository.findByDriverId(id).get().stream().map(RaceDriver::getRace).toList();
    }

    public List<RaceViewModel> getRacesAsViewModels(Integer id) {
        List<Race> races = driverRaceRepository.findByDriverId(id)
                .get()
                .stream()
                .map(RaceDriver::getRace)
                .toList();

        return races.stream()
                .map(this::mapRaceToViewModel)
                .toList();
    }

    private RaceViewModel mapRaceToViewModel(Race race) {
        RaceViewModel viewModel = new RaceViewModel();
        viewModel.setId(race.getId());
        viewModel.setName(race.getName());
        viewModel.setDate(race.getDate());
        viewModel.setHasEnded(race.isHasEnded());

        Track track = race.getTrack();
        if (track != null) {
            viewModel.setTrackId(track.getId());
            viewModel.setTrackName(track.getName());
        }

        Driver winner = race.getWinner();
        if (winner != null) {
            viewModel.setWinnerId(winner.getId());
            viewModel.setWinnerName(winner.getName());
        }

        return viewModel;
    }

}
