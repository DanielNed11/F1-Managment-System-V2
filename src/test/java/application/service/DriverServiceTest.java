package application.service;

import application.TestHelper;
import application.domain.AppUser;
import application.domain.Driver;
import application.domain.Team;
import application.service.command.UpdateDriverCommand;
import application.service.impl.DriverService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DriverServiceTest {

    @Autowired
    private DriverService sut;
    @Autowired
    private TestHelper testHelper;

    @Test
    void managerCanUpdateDriverFromManagedTeam() {
        // Arrange
        Team team = testHelper.team("Levski");
        AppUser dani = testHelper.user("Dani", team);

        Driver driver = testHelper.driver("Ivan", team);

        UpdateDriverCommand updateDriverCommand = new UpdateDriverCommand();
        updateDriverCommand.setId(driver.getId());
        updateDriverCommand.setName("Gosho");
        updateDriverCommand.setTeamId(team.getId());
        updateDriverCommand.setWorldChampionships(5);

        // Act
        Driver updatedDriver = sut.update(updateDriverCommand, dani.getId());

        // Assert
        assertEquals(driver.getId(), updatedDriver.getId());
        assertEquals("Gosho", updatedDriver.getName());
        assertEquals(5, updatedDriver.getWorldChampionships());

    }

    @Test
    void managerCannotUpdateDriverFromDifferentTeam() {
        // Arrange
        Team team = testHelper.team("Levski");
        Team otherTeam = testHelper.team("CSKA");
        AppUser dani = testHelper.user("Dani", team);

        Driver driver = testHelper.driver("Ivan", otherTeam);

        UpdateDriverCommand updateDriverCommand = new UpdateDriverCommand();
        updateDriverCommand.setId(driver.getId());
        updateDriverCommand.setName("Gosho");
        updateDriverCommand.setTeamId(team.getId());
        updateDriverCommand.setWorldChampionships(5);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> sut.update(updateDriverCommand, dani.getId()));
    }


    @AfterEach
    public void tearDown() {
        testHelper.cleanUp();
    }

}
