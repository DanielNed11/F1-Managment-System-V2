package application.service;

import application.TestHelper;
import application.domain.Driver;
import application.domain.Race;
import application.domain.Team;
import application.domain.Track;
import application.service.command.RaceDriverSelectionCommand;
import application.service.command.UpdateRaceCommand;
import application.service.impl.RaceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class RaceServiceTest {

    @Autowired
    private RaceService raceService;
    @Autowired
    private TestHelper testHelper;

    @Test
    void updateRaceTest() {
        // Arrange
        Team team = testHelper.team("Levski");
        Driver dani = testHelper.driver("Dani", team);
        Track track = testHelper.track("Gerena");
        Race beforeUpdate = testHelper.race("Race Test", track, dani, dani);
        Driver ivan = testHelper.driver("Ivan", team);

        UpdateRaceCommand updateRaceCommand = new UpdateRaceCommand();
        updateRaceCommand.setId(beforeUpdate.getId());
        updateRaceCommand.setName("Different Name");
        updateRaceCommand.setWinnerId(ivan.getId());
        RaceDriverSelectionCommand selection = new RaceDriverSelectionCommand();
        selection.setDriverId(ivan.getId());
        selection.setParticipated(true);
        selection.setPosition(1);
        updateRaceCommand.setDriverSelections(List.of(selection));

        // Act
        Race afterUpdate = raceService.updateRace(updateRaceCommand);

        // Assert
        assertNotNull(afterUpdate);
        assertEquals(beforeUpdate.getId(), afterUpdate.getId());
        assertEquals("Different Name", afterUpdate.getName());
        assertEquals("Ivan", afterUpdate.getWinner().getName());

    }

    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }
}
