package application.service.impl;

import application.domain.Team;
import application.domain.League;
import application.repository.IRepository;
import application.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService implements ITeamService {

    private final IRepository<Team> teamRepository;

    @Autowired
    public TeamService(IRepository<Team> teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> getAll() {
        return teamRepository.getAll();
    }

    @Override
    public Team getById(int id) {
        return teamRepository.getById(id);
    }

    @Override
    public void add(Team team) {
        teamRepository.add(team);
    }

    @Override
    public void update(Team team) {
        teamRepository.update(team);
    }

    @Override
    public List<Team> filterTeams(League league) {
        return teamRepository.getAll().stream()
                .filter(t -> league == null || t.getLeague() == league)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int id) {
        teamRepository.delete(id);
    }


}
