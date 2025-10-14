package application.service;

import application.domain.Driver;
import java.time.LocalDate;
import java.util.List;

public interface IDriverService extends IService<Driver> {
    List<Driver> filterDrivers(String nationality, LocalDate dateOfBirth);
    void validateDriver(Driver driver);
}
