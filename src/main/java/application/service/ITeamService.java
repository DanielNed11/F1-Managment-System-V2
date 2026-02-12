package application.service;

import application.domain.League;
import application.domain.Team;

import java.util.List;

public interface ITeamService extends IService<Team> {
    List<Team> filterTeams(League league);
}
