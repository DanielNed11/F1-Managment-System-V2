package application.api;

import application.api.dto.SimpleTeamDTO;
import application.mapper.TeamMapper;
import application.service.ITeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
