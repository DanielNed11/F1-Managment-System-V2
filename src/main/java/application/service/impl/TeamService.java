package application.service.impl;

import application.domain.League;
import application.domain.Team;
import application.repository.ITeamRepository;
import application.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService implements ITeamService {

    private final ITeamRepository teamRepository;

    @Autowired
    public TeamService(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> getAllWithDrivers() {
        return teamRepository.findAllWithDrivers();
    }



    @Override
    public Team getByIdWithDrivers(Integer id) {
        return teamRepository.findByIdWithDrivers(id).orElse(null);
    }

    @Override
    public Team getById(Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Override
    public void add(Team team) {
        teamRepository.save(team);
    }

    @Override
    public void update(Team team) {
        teamRepository.save(team);
    }

    @Override
    public void delete(Integer id) {
        teamRepository.deleteById(id);
    }

    @Override
    public List<Team> filterTeams(League league) {
        return teamRepository.findAllWithDrivers().stream()
                .filter(t -> league == null || t.getLeague() == league)
                .collect(Collectors.toList());
    }


}
