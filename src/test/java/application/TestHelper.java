package application;

import application.domain.*;
import application.repository.*;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {

    private final IDriverRepository driverRepository;
    private final IRaceRepository raceRepository;
    private final ITrackRepository trackRepository;
    private final ITeamRepository teamRepository;
    private final IDriverRaceRepository driverRaceRepository;
    private final IAppUserRepository iAppUserRepository;

    public TestHelper(IDriverRepository driverRepository,
                      IRaceRepository raceRepository,
                      ITrackRepository trackRepository,
                      ITeamRepository teamRepository,
                      IDriverRaceRepository driverRaceRepository, IAppUserRepository iAppUserRepository) {
        this.driverRepository = driverRepository;
        this.raceRepository = raceRepository;
        this.trackRepository = trackRepository;
        this.teamRepository = teamRepository;
        this.driverRaceRepository = driverRaceRepository;
        this.iAppUserRepository = iAppUserRepository;
    }

    public AppUser user(String username, Team team) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword("password");
        appUser.setRole(Role.USER);
        appUser.setManagerInTeam(team);

        return iAppUserRepository.save(appUser);
    }

    public AppUser admin(String username, Team team) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword("password");
        appUser.setRole(Role.ADMIN);
        appUser.setManagerInTeam(team);

        return iAppUserRepository.save(appUser);
    }

    public Driver driver(String name, Team team) {
        Driver driver = new Driver();

        driver.setTeam(team);
        driver.setName(name);

        return driverRepository.save(driver);
    }

    public Team team(String name) {
        Team team = new Team();
        team.setName(name);
        return teamRepository.save(team);
    }

    public Race race(String name, Track track, Driver winner, Driver... drivers) {
        Race race = new Race();
        race.setName(name);
        race.setTrack(track);
        race.setWinner(winner);
        for (Driver driver : drivers) {
            race.addRaceDriver(driver);
        }

        return raceRepository.save(race);
    }

    public Track track(String name) {
        Track track = new Track();
        track.setName(name);
        return trackRepository.save(track);
    }

    public void cleanUp() {
        driverRaceRepository.deleteAll();
        raceRepository.deleteAll();
        driverRepository.deleteAll();
        iAppUserRepository.deleteAll();
        teamRepository.deleteAll();
        trackRepository.deleteAll();
    }

}
