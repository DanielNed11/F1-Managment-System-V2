package application.controller;

import application.domain.Driver;
import application.domain.League;
import application.mapper.DriverMapper;
import application.service.IDriverService;
import application.service.impl.TeamService;
import application.viewmodel.DriverDTO;
import application.viewmodel.TeamDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final IDriverService driverService;
    private final Log log = LogFactory.getLog(this.getClass());
    private final DriverMapper driverMapper;

    @Autowired
    public TeamController(TeamService teamService, IDriverService driverService, DriverMapper driverMapper) {
        this.teamService = teamService;
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @GetMapping
    public String getTeams(Model model,
                           @RequestParam(required = false)
                           League league) {

        List<TeamDTO> teamDTOS = teamService.filterTeams(league);

        model.addAttribute("league", league);
        model.addAttribute("teams", teamDTOS);
        log.info("Getting teams");
        return "teams/teams";
    }

    @GetMapping("/{id}")
    public String getTeam(Model model, @PathVariable Integer id) {
        TeamDTO teamDTO = teamService.getById(id);
        if (teamDTO == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }

        List<Driver> drivers = driverService.findByTeamId(id);

        model.addAttribute("team", teamDTO);
        model.addAttribute("drivers", driverMapper.toDriverDTOList(drivers));
        log.info("Getting team with id " + id);
        return "teams/team";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddTeamForm(Model model) {

        model.addAttribute("teamDTO", new TeamDTO());
        log.info("Showing add team form");
        return "teams/add-team";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTeam(@Valid @ModelAttribute TeamDTO teamDTO,
                          BindingResult bindingResult
                         ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when adding team");
            return "teams/add-team";
        }

        teamService.add(teamDTO);
        log.info("Added team with id " + teamDTO.getId());
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateTeamForm(Model model, @PathVariable Integer id) {
        TeamDTO teamDTO = teamService.getById(id);
        if (teamDTO == null) {
            return "redirect:/teams";
        }

        model.addAttribute("teamDTO", teamDTO);
        log.info("Show edit form for team with id " + id);
        return "teams/edit-team";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTeam(@PathVariable Integer id,
                             @Valid @ModelAttribute TeamDTO teamDTO,
                             BindingResult bindingResult) {
        TeamDTO oldTeam = teamService.getById(id);
        if (oldTeam == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when updating team with id " + id);
            return "teams/edit-team";
        }

        teamService.update(teamDTO);
        log.info("Updated team with id " + id);
        return "redirect:/teams";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTeam(@PathVariable Integer id, HttpSession session) {
        teamService.delete(id);
        log.info("Deleted team with id " + id + " for session: " + session.getId());
        return "redirect:/teams";
    }

}
