package application.mapper;

import application.domain.RaceDriver;
import application.domain.Race;
import application.viewmodel.RaceDTO;
import application.viewmodel.RaceDriverDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DriverMapper.class)
public interface RaceMapper {

    Race toRace(RaceDTO raceDTO);

    @Mapping(target = "trackId", source = "track.id")
    @Mapping(target = "trackName", source = "track.name")
    @Mapping(target = "winnerId", source = "winner.id")
    @Mapping(target = "winnerName", source = "winner.name")
    RaceDTO toRaceDTO(Race race);

    RaceDriver toRaceDriver(RaceDriverDTO raceDriverDTO);

    List<RaceDTO> toRaceDTOList(List<Race> races);
    RaceDriverDTO toRaceDriverDTO(RaceDriver raceDriver);
    List<RaceDriverDTO> toRaceDriverDTOList(List<RaceDriver> raceDrivers);
}
