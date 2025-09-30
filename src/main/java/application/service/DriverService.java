package application.service;

import application.domain.Driver;
import application.repository.DataFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    public List<Driver> getDrivers() {
        return DataFactory.drivers;
    }
}
