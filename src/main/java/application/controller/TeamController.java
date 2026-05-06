package application.controller;

import application.controller.viewmodel.TeamViewModel;
import application.domain.Driver;
import application.domain.League;
import application.domain.Team;
import application.mapper.DriverMapper;
import application.mapper.TeamMapper;
import application.service.ITeamService;
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
import java.util.Objects;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final ITeamService teamService;
    private final Log log = LogFactory.getLog(this.getClass());
    private final DriverMapper driverMapper;
    private final TeamMapper teamMapper;

    @Autowired
    public TeamController(ITeamService teamService,
                          DriverMapper driverMapper,
                          TeamMapper teamMapper) {
        this.teamService = teamService;
        this.driverMapper = driverMapper;
        this.teamMapper = teamMapper;
    }

    @GetMapping
    public String getTeams(Model model,
                           @RequestParam(required = false)
                           League league) {

        List<TeamViewModel> teamViewModels = teamMapper.toViewModelList
                (teamService.filterTeams(league));

        model.addAttribute("league", league);
        model.addAttribute("teams", teamViewModels);
        log.info("Getting teams");
        return "teams/teams";
    }

    @GetMapping("/{id}")
    public String getTeam(Model model, @PathVariable Integer id) {

        Team team = teamService.getByIdWithDrivers(id);

        TeamViewModel teamViewModel = teamMapper.toViewModel(team);

        if (teamViewModel == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }

        List<Driver> drivers = team.getDrivers();

        model.addAttribute("team", teamViewModel);
        model.addAttribute("drivers", driverMapper.toDriverViewModelList(drivers));
        log.info("Getting team with id " + id);
        return "teams/team";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddTeamForm(Model model) {

        model.addAttribute("teamViewModel", new TeamViewModel());
        log.info("Showing add team form");
        return "teams/add-team";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTeam(@Valid @ModelAttribute TeamViewModel teamViewModel,
                          BindingResult bindingResult
                         ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when adding team");
            return "teams/add-team";
        }

        teamService.add(teamMapper.toTeam(teamViewModel));

        log.info("Added team with id " + teamViewModel.getId());
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateTeamForm(Model model, @PathVariable Integer id) {

        TeamViewModel teamViewModel = teamMapper.toViewModel(teamService.getById(id));

        if (teamViewModel == null) {
            return "redirect:/teams";
        }

        model.addAttribute("teamViewModel", teamViewModel);
        log.info("Show edit form for team with id " + id);
        return "teams/edit-team";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTeam(@PathVariable Integer id,
                             @Valid @ModelAttribute TeamViewModel teamViewModel,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when updating team with id " + id);
            return "teams/edit-team";
        }

        Team team = teamMapper.toTeam(teamViewModel);

        team.setId(id);

        teamService.update(team);
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
