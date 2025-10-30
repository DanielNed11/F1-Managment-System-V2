package application.service.impl;

import application.domain.*;
import application.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final TrackRepository trackRepository;
    private final RaceRepository raceRepository;

    @Autowired
    public DataInitializer(DriverRepository driverRepository, TeamRepository teamRepository,
                           TrackRepository trackRepository,  RaceRepository raceRepository) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
        this.trackRepository = trackRepository;
        this.raceRepository = raceRepository;
    }

    @PostConstruct
    public void initData() {
        List<Driver> drivers = driverRepository.getAll();
        List<Team> teams = teamRepository.getAll();
        List<Track> tracks = trackRepository.getAll();
        List<Race> races = raceRepository.getAll();

        Team mercedes = teams.stream().filter(t -> t.getName().equals("Mercedes")).findFirst().orElse(null);
        Team ferrari = teams.stream().filter(t -> t.getName().equals("Ferrari")).findFirst().orElse(null);
        Team redBull = teams.stream().filter(t -> t.getName().equals("Red Bull Racing")).findFirst().orElse(null);

        Driver hamilton = drivers.stream().filter(d -> d.getName().equals("Lewis Hamilton")).findFirst().orElse(null);
        Driver verstappen = drivers.stream().filter(d -> d.getName().equals("Max Verstappen")).findFirst().orElse(null);
        Driver leclerc = drivers.stream().filter(d -> d.getName().equals("Charles Leclerc")).findFirst().orElse(null);
        Driver sainz = drivers.stream().filter(d -> d.getName().equals("Carlos Sainz")).findFirst().orElse(null);

        if (mercedes != null && hamilton != null) mercedes.addDriver(hamilton);
        if (redBull != null && verstappen != null) redBull.addDriver(verstappen);
        if (ferrari != null && leclerc != null) ferrari.addDriver(leclerc);
        if (ferrari != null && sainz != null) ferrari.addDriver(sainz);

        Track monaco = tracks.stream().filter(t -> t.getName().equals("Monaco GP")).findFirst().orElse(null);
        Track silverstone = tracks.stream().filter(t -> t.getName().equals("Silverstone")).findFirst().orElse(null);

        Race monacoGrandPrix = races.stream().filter(r -> r.getName().equals("Monaco Grand Prix")).findFirst().orElse(null);
        Race britishGrandPrix = races.stream().filter(r -> r.getName().equals("British Grand Prix")).findFirst().orElse(null);

        monacoGrandPrix.setTrack(monaco);
        britishGrandPrix.setTrack(silverstone);

        monacoGrandPrix.addDriver(hamilton);
        monacoGrandPrix.addDriver(leclerc);
        britishGrandPrix.addDriver(verstappen);
        britishGrandPrix.addDriver(hamilton);

        monacoGrandPrix.setWinner(hamilton);
        britishGrandPrix.setWinner(verstappen);
    }
}
