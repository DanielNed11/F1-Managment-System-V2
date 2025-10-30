package application.service.impl;

import application.domain.Driver;
import application.domain.Team;
import application.repository.DriverRepository;
import application.service.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DriverService implements IDriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
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

        Team oldTeam = existingDriver.getTeam();
        Team newTeam = updatedDriver.getTeam();


        if (oldTeam != null && (newTeam == null || oldTeam.getId() != newTeam.getId())) {
            oldTeam.getDrivers().remove(existingDriver);
            existingDriver.setTeam(null);
        }


        if (newTeam != null && !newTeam.getDrivers().contains(updatedDriver)) {
            newTeam.getDrivers().add(updatedDriver);
        }

        driverRepository.update(updatedDriver);
    }

    @Override
    public List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth) {
        return driverRepository.getAll().stream()
                .filter(d -> nationality == null || nationality.isBlank() ||
                        d.getNationality().toLowerCase().contains(nationality.trim().toLowerCase()))
                .filter(d -> dateOfBirth == null || d.getDateOfBirth().equals(dateOfBirth))
                .toList();
    }

    @Override
    public void validateDriver(Driver driver) {

    }

}
