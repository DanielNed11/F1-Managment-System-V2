package application.service;

import application.domain.Driver;
import application.domain.Race;
import application.service.command.UpdateDriverCommand;

import java.time.LocalDate;
import java.util.List;

public interface IDriverService {
    List<Driver> getAll();

    Driver getById(Integer id);

    Driver add(Driver driver, int appUserId);

    Driver update(UpdateDriverCommand driverCommand, int appUserId);

    void delete(Integer id, int appUserId);

    List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth);

    List<Driver> findChampions();

    List<Race> getRacesByDriver(Integer id);

    boolean canModifyDriver(Integer driverId, int appUserId);
}
