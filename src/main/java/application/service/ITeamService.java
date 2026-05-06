package application.service;

import application.domain.League;
import application.domain.Team;

import java.util.List;

public interface ITeamService {
    List<Team> getAllWithDrivers();

    Team getByIdWithDrivers(Integer id);

    Team getById(Integer id);

    List<Team> getAll();

    Team add(Team entity);

    void update(Team entity);

    void delete(Integer id);

    List<Team> filterTeams(League league);
}
