package application.service;

import application.domain.AppUser;
import application.domain.Driver;
import application.domain.Role;
import application.domain.Team;
import application.repository.IAppUserRepository;
import application.repository.IDriverRaceRepository;
import application.repository.IDriverRepository;
import application.repository.ITeamRepository;
import application.service.command.UpdateDriverCommand;
import application.service.impl.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ActiveProfiles("test")
public class DriverServiceUnitTest {

    private IDriverService sut;

    private IDriverRepository driverRepository;
    private IAppUserRepository appUserRepository;
    private ITeamRepository teamRepository;

    private Team team;
    private AppUser appUser;
    private Driver driver;

    @BeforeEach
    void setUp() {
        driverRepository = mock(IDriverRepository.class);
        appUserRepository = mock(IAppUserRepository.class);
        teamRepository = mock(ITeamRepository.class);

        sut = new DriverService(
                driverRepository,
                mock(IDriverRaceRepository.class),
                appUserRepository,
                teamRepository
        );

        team = new Team();
        team.setId(1);

        appUser = new AppUser();
        appUser.setId(1);
        appUser.setManagerInTeam(team);

        driver = new Driver();
        driver.setId(1);
        driver.setName("Dani");
        driver.setNationality("Bulgaria");
        driver.setWorldChampionships(1);
    }

    @Test
    void managerCanUpdateDriverFromManagedTeam() {
        // Arrange
        appUser.setRole(Role.USER);
        driver.setTeam(team);

        UpdateDriverCommand updateDriverCommand = new UpdateDriverCommand();
        updateDriverCommand.setId(1);
        updateDriverCommand.setName("Ivan");
        updateDriverCommand.setNationality("Macedonia");
        updateDriverCommand.setWorldChampionships(0);

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(driverRepository.save(driver)).willReturn(driver);
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));
        // Act
        Driver updated = sut.update(updateDriverCommand, appUser.getId());

        // Assert
        assertEquals("Ivan", updated.getName());
        assertEquals("Macedonia", updated.getNationality());
        assertEquals(team, updated.getTeam());
        assertEquals(0, updated.getWorldChampionships());

        verify(driverRepository).save(driver);
    }

    @Test
    void managerCannotUpdateDriverFromDifferentTeam() {
        appUser.setRole(Role.USER);
        Team anotherTeam = new Team();
        anotherTeam.setId(2);
        driver.setTeam(anotherTeam);

        UpdateDriverCommand updateDriverCommand = new UpdateDriverCommand();
        updateDriverCommand.setId(1);
        updateDriverCommand.setName("Ivan");
        updateDriverCommand.setNationality("Macedonia");
        updateDriverCommand.setWorldChampionships(0);

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> sut.update(updateDriverCommand, appUser.getId()));

        verify(driverRepository, never()).save(driver);
    }

    @Test
    void adminCanUpdateDriverFromAnyTeam() {
        appUser.setRole(Role.ADMIN);

        Team anotherTeam = new Team();
        anotherTeam.setId(2);
        driver.setTeam(anotherTeam);

        UpdateDriverCommand updateDriverCommand = new UpdateDriverCommand();
        updateDriverCommand.setId(1);
        updateDriverCommand.setName("Ivan");
        updateDriverCommand.setNationality("Macedonia");
        updateDriverCommand.setWorldChampionships(0);
        updateDriverCommand.setTeamId(team.getId());

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));
        given(teamRepository.findById(1)).willReturn(Optional.of(team));
        given(driverRepository.save(driver)).willReturn(driver);

        // Act
        Driver updated = sut.update(updateDriverCommand, appUser.getId());

        // Assert
        assertEquals("Ivan", updated.getName());
        assertEquals("Macedonia", updated.getNationality());
        assertEquals(team, updated.getTeam());
        assertEquals(0, updated.getWorldChampionships());

        verify(driverRepository).save(driver);
    }

    @Test
    void managerCanDeleteDriverFromManagedTeam() {
        // Arrange
        appUser.setRole(Role.USER);
        driver.setTeam(team);

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));

        // Act
        sut.delete(driver.getId(), appUser.getId());

        // Assert
        verify(driverRepository).deleteById(driver.getId());
    }

    @Test
    void managerCannotDeleteDriverFromOtherTeams() {
        // Arrange
        appUser.setRole(Role.USER);
        Team anotherTeam = new Team();
        anotherTeam.setId(2);
        driver.setTeam(anotherTeam);

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> sut.delete(driver.getId(), appUser.getId()));

        verify(driverRepository, never()).deleteById(driver.getId());
    }

    @Test
    void adminCanDeleteDriverFromAnyTeam() {
        // Arrange
        appUser.setRole(Role.ADMIN);

        Team anotherTeam = new Team();
        anotherTeam.setId(2);
        driver.setTeam(anotherTeam);

        given(driverRepository.findById(1)).willReturn(Optional.of(driver));
        given(appUserRepository.findById(1)).willReturn(Optional.of(appUser));

        // Act
        sut.delete(driver.getId(), appUser.getId());

        // Assert
        verify(driverRepository).deleteById(driver.getId());
    }
}
