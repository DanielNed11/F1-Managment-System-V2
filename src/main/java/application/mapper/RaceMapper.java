package application.mapper;

import application.api.dto.RaceDTO;
import application.controller.viewmodel.*;
import application.domain.RaceDriver;
import application.domain.Race;
import application.service.command.AddRaceCommand;
import application.service.command.RaceDriverSelectionCommand;
import application.service.command.UpdateRaceCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DriverMapper.class)
public interface RaceMapper {

    @Mapping(target = "trackId", source = "track.id")
    @Mapping(target = "trackName", source = "track.name")
    @Mapping(target = "winnerId", source = "winner.id")
    @Mapping(target = "winnerName", source = "winner.name")
    RaceViewModel toRaceViewModel(Race race);

    @Mapping(target = "trackId", source = "track.id")
    @Mapping(target = "trackName", source = "track.name")
    @Mapping(target = "winnerId", source = "winner.id")
    @Mapping(target = "winnerName", source = "winner.name")
    RaceDTO toRaceDTO(Race race);

    List<RaceViewModel> toRaceViewModelList(List<Race> races);
    RaceDriverViewModel toRaceDriverViewModel(RaceDriver raceDriver);
    List<RaceDriverViewModel> toRaceDriverViewModelList(List<RaceDriver> raceDrivers);

    List<RaceDTO> toRaceDTOs(List<Race> racesByDriver);

    UpdateRaceCommand toUpdateRaceCommand(EditRaceViewModel editRaceViewModel);

    AddRaceCommand toAddRaceCommand(AddRaceViewModel addRaceViewModel);


    RaceDriverSelectionCommand toRaceDriverSelectionCommand(RaceDriverSelectionViewModel raceDriverSelectionViewModel);
}
