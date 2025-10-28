package application.controller;

import application.domain.League;
import application.domain.Team;
import application.service.impl.TeamService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public String getTeams(Model model,
                           @RequestParam(required = false)
                           League league) {
        model.addAttribute("league", league);
        model.addAttribute("teams", teamService.filterTeams(league));
        log.info("Getting teams");
        return "teams/teams";
    }

    @GetMapping("/{id}")
    public String getTeam(Model model, @PathVariable int id) {
        model.addAttribute("team", teamService.getById(id));
        log.info("Getting team with id " + id);
        return "teams/team";
    }

    @GetMapping("/add")
    public String showAddTeamForm(Model model) {
        model.addAttribute("team", new Team());
        log.info("Showing add team form");
        return "teams/add-team";
    }

    @PostMapping("/add")
    public String addTeam(@ModelAttribute Team team) {
        teamService.add(team);
        log.info("Added team with id " + team.getId());
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateTeamForm(Model model, @PathVariable int id) {
        Team team = teamService.getById(id);
        if (team == null) {
            return "redirect:/teams";
        }
        model.addAttribute("team", team);
        log.info("Show Edit Form");
        return "teams/edit-team";
    }

    @PostMapping("/edit/{id}")
    public String updateTeam(@PathVariable int id, @ModelAttribute Team team) {
        Team oldTeam = teamService.getById(id);
        if (oldTeam == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }
        teamService.update(team);
        log.info("Updated team with id " + id);
        return "redirect:/teams";
    }

}
