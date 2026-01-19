package application.service.impl;

import application.domain.Driver;
import application.domain.Team;
import application.exception.F1ApplicationException;
import application.repository.IDriverRepository;
import application.service.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DriverService implements IDriverService {

    private final IDriverRepository driverRepository;

    @Autowired
    public DriverService(IDriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getAll() {
        return driverRepository.getAll();
    }

    @Override
    public Driver getById(int id) {
        return driverRepository.getById(id);
    }

    @Override
    public void add(Driver driver) {
        driverRepository.add(driver);
    }

    @Override
    public void update(Driver updatedDriver) {
        Driver existingDriver = driverRepository.getById(updatedDriver.getId());
        if (existingDriver == null) {
            throw new IllegalArgumentException("Driver with id " + updatedDriver.getId() + " not found");
        }

        driverRepository.update(updatedDriver);
    }

    @Override
    public List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth) {
        return driverRepository.getAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || (d.getDateOfBirth() != null && d.getDateOfBirth().equals(dateOfBirth)))
                .toList();
    }

    @Override
    public void validateDriver(Driver driver) {
        if (driver == null) {
            throw new F1ApplicationException("Driver cannot be null");
        }

        if (driver.getDateOfBirth() == null) {
            throw new F1ApplicationException("Driver date of birth is required");
        }

        int age = Period.between(driver.getDateOfBirth(), LocalDate.now()).getYears();

        if (age < 18) {
            throw new F1ApplicationException(
                    "Driver must be at least 18 years old. Current age: " + age + " years");
        }

        if (age > 60) {
            throw new F1ApplicationException(
                    "Driver age exceeds maximum racing age of 60. Current age: " + age + " years");
        }

        if (driver.getWorldChampionships() < 0) {
            throw new F1ApplicationException("World championships cannot be negative");
        }
    }

    @Override
    public void delete(int id) {
        driverRepository.delete(id);
    }

    @Override
    public List<Driver> findChampions() {
        // Now all repository implementations have this method!
        return driverRepository.findByWorldChampionshipsGreaterThan(0);
    }

    @Override
    public List<Driver> findByTeamId(Integer teamId) {
        return driverRepository.findByTeamId(teamId);
    }

}
