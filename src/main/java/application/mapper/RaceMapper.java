package application.mapper;

import application.domain.RaceDriver;
import application.domain.Race;
import application.viewmodel.RaceDTO;
import application.viewmodel.RaceDriverDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DriverMapper.class)
public interface RaceMapper {

    Race toRace(RaceDTO raceDTO);
    RaceDTO toRaceDTO(Race race);
    List<RaceDTO> toRaceDTOList(List<Race> races);
    RaceDriverDTO toRaceDriverDTO(RaceDriver raceDriver);
    List<RaceDriverDTO> toRaceDriverDTOList(List<RaceDriver> raceDrivers);
}
