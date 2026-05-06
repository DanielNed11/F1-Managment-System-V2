package application.mapper;

import application.api.dto.SimpleTeamDTO;
import application.domain.Team;
import application.controller.viewmodel.TeamViewModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamViewModel toViewModel(Team team);
    Team toTeam(TeamViewModel teamViewModel);
    List<TeamViewModel> toViewModelList(List<Team> teams);
    SimpleTeamDTO toDTO(Team team);
    List<SimpleTeamDTO> toDTOList(List<Team> teams);
}
