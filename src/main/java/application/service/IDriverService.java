package application.service;

import application.domain.Driver;
import application.domain.Race;
import application.viewmodel.AddDriverDto;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import application.viewmodel.RaceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IDriverService {
    List<Driver> getAll();

    Driver getById(Integer id);

    Driver add(AddDriverDto entity, int appUserId);

    Driver update(Integer id, PatchDriverDTO patchDriverDTO, int appUserId);

    void delete(Integer id, int appUserId);

    List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth);

    List<Driver> findChampions();

    List<Driver> findByTeamId(Integer teamId);

    List<Race> getRacesByDriver(Integer id);

    boolean canModifyDriver(Integer driverId, int appUserId);
}
