package application.repository;

import application.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DriverRepository implements IRepository<Driver> {
    private final List<Driver> drivers = new ArrayList<>();

    public DriverRepository() {
        this.add(new Driver("Lewis Hamilton", LocalDate.of(1985, 1, 7), "British", 7));
        this.add( new Driver( "Max Verstappen", LocalDate.of(1997, 9, 30), "Dutch", 2));
        this.add(new Driver( "Charles Leclerc", LocalDate.of(1997, 10, 16), "Monegasque", 0));
        this.add(new Driver( "Carlos Sainz", LocalDate.of(1994, 9, 1), "Spanish", 0));
        this.add( new Driver( "Valtteri Bottas", LocalDate.of(1989, 8, 28), "Finnish", 0));
    }

    @Override
    public List<Driver> getAll() {
        return drivers;
    }

    @Override
    public Driver getById(int id) {
        return drivers.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void add(Driver driver) {
        driver.setId(drivers.size() + 1);
        drivers.add(driver);
    }

    @Override
    public void update(Driver driver) {
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId() == driver.getId()) {
                LocalDate dateOfBirth = drivers.get(i).getDateOfBirth();
                driver.setDateOfBirth(dateOfBirth);
                drivers.set(i, driver);
                return;
            }
        }
        throw new IllegalArgumentException("Driver with id " + driver.getId() + " not found");
    }
}
