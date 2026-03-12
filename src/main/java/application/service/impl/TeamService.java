package application.service.impl;

import application.domain.League;
import application.mapper.TeamMapper;
import application.repository.ITeamRepository;
import application.service.ITeamService;
import application.viewmodel.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService implements ITeamService {

    private final ITeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Autowired
    public TeamService(ITeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public List<TeamDTO> getAll() {
        return teamMapper.toTeamDTOList(teamRepository.findAll());
    }

    @Override
    public TeamDTO getById(Integer id) {
        return teamMapper.toTeamDTO(teamRepository.findById(id).get());
    }

    @Override
    public void add(TeamDTO team) {
        teamRepository.save(teamMapper.toTeam(team));
    }

    @Override
    public void update(TeamDTO team) {
        teamRepository.save(teamMapper.toTeam(team));
    }

    @Override
    public void delete(Integer id) {
        teamRepository.deleteById(id);
    }

    @Override
    public List<TeamDTO> filterTeams(League league) {
        return teamMapper.toTeamDTOList(teamRepository.findAll().stream()
                .filter(t -> league == null || t.getLeague() == league)
                .collect(Collectors.toList()));
    }


}
