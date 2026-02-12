package application.service.impl;

import application.domain.League;
import application.domain.Team;
import application.repository.ITeamRepository;
import application.service.ITeamService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TeamService implements ITeamService {

    private final ITeamRepository teamRepository;

    @Autowired
    public TeamService(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Override
    public Team getById(Integer id) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team != null) {
            Hibernate.initialize(team.getDrivers());
        }
        return team;
    }

    @Override
    @Transactional
    public void add(Team team) {
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void update(Team team) {
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        teamRepository.deleteById(id);
    }

    @Override
    public List<Team> filterTeams(League league) {
        List<Team> teams = teamRepository.findAll().stream()
                .filter(t -> league == null || t.getLeague() == league)
                .collect(Collectors.toList());
        teams.forEach(t -> Hibernate.initialize(t.getDrivers()));
        return teams;
    }


}
