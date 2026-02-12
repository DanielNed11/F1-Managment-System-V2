package application.service.impl;

import application.domain.Driver;
import application.repository.IDriverRepository;
import application.service.IDriverService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DriverService implements IDriverService {

    private final IDriverRepository driverRepository;

    @Autowired
    public DriverService(IDriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getById(Integer id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver != null) {
            Hibernate.initialize(driver.getTeam());
            Hibernate.initialize(driver.getDriverRaces());
        }
        return driver;
    }

    @Override
    @Transactional
    public void add(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void update(Driver updatedDriver) {
        Driver existingDriver = driverRepository.findById(updatedDriver.getId()).orElse(null);
        if (existingDriver == null) {
            throw new IllegalArgumentException("Driver with id " + updatedDriver.getId() + " not found");
        }

        driverRepository.save(updatedDriver);
    }

    @Override
    public List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth) {
        List<Driver> drivers = driverRepository.findAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || (d.getDateOfBirth() != null && d.getDateOfBirth().equals(dateOfBirth)))
                .toList();
        drivers.forEach(d -> Hibernate.initialize(d.getTeam()));
        return drivers;
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
    @Transactional
    public void delete(Integer id) {
        driverRepository.deleteById(id);
    }

    @Override
    public List<Driver> findChampions() {
        List<Driver> champions = driverRepository.findByWorldChampionshipsGreaterThan(0);
        champions.forEach(d -> Hibernate.initialize(d.getTeam()));
        return champions;
    }

    @Override
    public List<Driver> findByTeamId(Integer teamId) {
        List<Driver> drivers = driverRepository.findByTeamId(teamId);
        drivers.forEach(d -> Hibernate.initialize(d.getTeam()));
        return drivers;
    }

}
