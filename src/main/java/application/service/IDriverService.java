package application.service;

import application.viewmodel.DriverDTO;
import application.viewmodel.RaceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IDriverService {
    List<DriverDTO> getAll();

    DriverDTO getById(Integer id);

    void add(DriverDTO entity);

    void update(DriverDTO entity);

    void delete(Integer id);

    List<DriverDTO> filterDrivers(String nationality, LocalDate dateOfBirth);

    List<DriverDTO> findChampions();

    List<DriverDTO> findByTeamId(Integer teamId);

    List<RaceDTO> getRacesByDriver(Integer id);
}
