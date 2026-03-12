package application.service;

import application.domain.League;
import application.viewmodel.TeamDTO;

import java.util.List;

public interface ITeamService {
    List<TeamDTO> getAll();

    TeamDTO getById(Integer id);

    void add(TeamDTO entity);

    void update(TeamDTO entity);

    void delete(Integer id);
    List<TeamDTO> filterTeams(League league);
}
