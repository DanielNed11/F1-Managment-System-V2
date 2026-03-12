package application.mapper;

import application.domain.Team;
import application.viewmodel.TeamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toTeamDTO(Team team);
    Team toTeam(TeamDTO teamDTO);
    List<TeamDTO> toTeamDTOList(List<Team> teams);
}
