package application.api;

import application.api.dto.AddTeamDTO;
import application.api.dto.SimpleTeamDTO;
import application.api.dto.TeamDTO;
import application.domain.Team;
import application.mapper.TeamMapper;
import application.service.ITeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class ApiTeamController {

    private final ITeamService teamService;
    private final TeamMapper teamMapper;

    public ApiTeamController(ITeamService teamService, TeamMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    @GetMapping
    public ResponseEntity<List<SimpleTeamDTO>> getTeams() {
        return ResponseEntity.ok(teamMapper.toDTOList(teamService.getAllWithDrivers()));
    }

    @PostMapping
    public ResponseEntity<TeamDTO> addTeams(@Valid @RequestBody AddTeamDTO team) {

        Team added = teamService.add(teamMapper.toTeam(team));

        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toTeamDTO(added));
    }
}
