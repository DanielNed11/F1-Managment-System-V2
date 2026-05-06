package application.repository;

import application.TestHelper;
import application.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class RaceRepositoryTest {

    @Autowired
    private IRaceRepository sut;
    @Autowired
    private IDriverRaceRepository driverRaceRepository;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void deletingRaceDeletesRaceDrivers() {
        // Arrange
        Team team = testHelper.team("Levski");
        Driver driver = testHelper.driver("Dani", team);
        Track track = testHelper.track("Gerena");
        Race race = testHelper.race("Race Test", track, driver, driver);

        List<RaceDriver> oneRaceDriver = driverRaceRepository.findByRaceId(race.getId());

        // Act

        sut.delete(race);

        List<RaceDriver> none = driverRaceRepository.findByRaceId(race.getId());

        // Assert

        assertEquals(1, oneRaceDriver.size());
        assertEquals(0, none.size());

    }

    @Test
    public void findByIdFetchesRaceDetails() {
        // Arrange
        Team team = testHelper.team("Levski");
        Driver driver = testHelper.driver("Dani", team);
        Driver driver2 = testHelper.driver("Ivan", team);
        Track track = testHelper.track("Gerena");
        Race race = testHelper.race("Race Test", track, driver, driver, driver2);

        // Act
        Race testing = sut.findById(race.getId()).orElseThrow();

        // Assert
        assertEquals(race.getId(), testing.getId());
        assertEquals(race.getName(), testing.getName());
        assertEquals("Dani", testing.getWinner().getName());
        assertEquals("Gerena", testing.getTrack().getName());
        assertEquals(2, testing.getRaceDrivers().size());
        assertNotNull(testing.getRaceDrivers().get(0).getDriver());
        assertNotNull(testing.getRaceDrivers().get(1).getDriver());
    }

    @Test
    public void findRacesByDriverIdReturnsOnlyRacesForThatDriver() {
        // Arrange
        Team team = testHelper.team("Levski");
        Driver driver1 = testHelper.driver("Dani", team);
        Driver driver2 = testHelper.driver("Ivan", team);
        Track track = testHelper.track("Gerena");
        testHelper.race("all drivers", track, driver1, driver1, driver2);
        testHelper.race("driver2", track, driver2, driver2);
        testHelper.race("driver1", track, driver1, driver1);

        // Act
        List<Race> races = sut.findRacesByDriverId(driver1.getId());
        List<String> raceNamesForDriver1 = races.stream()
                .map(Race::getName).toList();

        // Assert
        assertEquals(2, races.size());
        assertTrue(raceNamesForDriver1.contains("all drivers"));
        assertTrue(raceNamesForDriver1.contains("driver1"));
        assertFalse(raceNamesForDriver1.contains("driver2"));

    }


    @AfterEach
    public void cleanup() {
        testHelper.cleanUp();
    }
}
