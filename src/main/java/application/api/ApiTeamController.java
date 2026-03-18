package application.api;

import application.mapper.TeamMapper;
import application.service.ITeamService;
import application.service.impl.TeamService;
import application.viewmodel.DriverDTO;
import application.viewmodel.TeamDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class ApiTeamController {

    private final ITeamService teamService;

    public ApiTeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getTeams() {
        return ResponseEntity.ok(teamService.getAll());
    }
}
