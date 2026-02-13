package application.service;

import application.domain.Driver;
import application.domain.Race;
import application.service.impl.RaceService;
import application.viewmodel.DriverViewModel;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface IDriverService extends IService<Driver> {
    List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth);

    void validateDriver(Driver driver);

    List<Driver> findChampions();

    List<Driver> findByTeamId(Integer teamId);

    Driver mapToDriver(@Valid DriverViewModel driverViewModel);

    DriverViewModel mapToViewModel(Driver driver);

    List<Race> getRaces(Integer id);
}
