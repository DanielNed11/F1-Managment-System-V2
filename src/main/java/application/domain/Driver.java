package application.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private ContractStatus contractStatus;
    private String nationality;
    private int worldChampionships;

    private F1Team team;
    private List<Track> tracksRaced = new ArrayList<>();

    public Driver(int id, String name, LocalDate dateOfBirth, String nationality,
                  int worldChampionships, ContractStatus contractStatus) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.worldChampionships = worldChampionships;
        this.contractStatus = contractStatus;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }
    public int getWorldChampionships() { return worldChampionships; }
    public F1Team getTeam() { return team; }
    public ContractStatus getContractStatus() { return contractStatus; }
    public List<Track> getTracksRaced() { return tracksRaced; }

    public void setTeam(F1Team team) { this.team = team; }

    public void addTrack(Track track) {
        tracksRaced.add(track);
        track.getDrivers().add(this);
    }

    @Override
    public String toString() {
        return name + " (" + nationality + ", Championships: " + worldChampionships + ", " + contractStatus + ")";
    }
}
