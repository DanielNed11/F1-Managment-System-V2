package application.controller;

import application.domain.Driver;
import application.domain.League;
import application.domain.Team;
import application.service.IDriverService;
import application.service.impl.TeamService;
import application.viewmodel.TeamViewModel;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final IDriverService driverService;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public TeamController(TeamService teamService, IDriverService driverService) {
        this.teamService = teamService;
        this.driverService = driverService;
    }

    @GetMapping
    public String getTeams(HttpSession session,
                           Model model,
                           @RequestParam(required = false)
                           League league) {

        List<Team> teams = teamService.filterTeams(league);
        List<TeamViewModel> teamViewModels = teams.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());

        model.addAttribute("league", league);
        model.addAttribute("teams", teamViewModels);
        log.info("Getting teams for session: " + session.getId());
        return "teams/teams";
    }

    @GetMapping("/{id}")
    public String getTeam(HttpSession session, Model model, @PathVariable Integer id) {
        Team team = teamService.getById(id);
        if (team == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }

        List<Driver> drivers = driverService.findByTeamId(id);

        TeamViewModel teamViewModel = mapToViewModel(team);
        model.addAttribute("team", teamViewModel);
        model.addAttribute("drivers", drivers);
        log.info("Getting team with id " + id);
        return "teams/team";
    }

    @GetMapping("/add")
    public String showAddTeamForm(HttpSession session, Model model) {

        model.addAttribute("teamViewModel", new TeamViewModel());
        log.info("Showing add team form");
        return "teams/add-team";
    }

    @PostMapping("/add")
    public String addTeam(@Valid @ModelAttribute TeamViewModel teamViewModel,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when adding team");
            return "teams/add-team";
        }

        Team team = mapToTeam(teamViewModel);
        teamService.add(team);
        log.info("Added team with id " + team.getId() + " for session: " + session.getId());
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateTeamForm(HttpSession session, Model model, @PathVariable Integer id) {
        Team team = teamService.getById(id);
        if (team == null) {
            return "redirect:/teams";
        }

        TeamViewModel teamViewModel = mapToViewModel(team);
        model.addAttribute("teamViewModel", teamViewModel);
        log.info("Show edit form for team with id " + id);
        return "teams/edit-team";
    }

    @PostMapping("/edit/{id}")
    public String updateTeam(@PathVariable Integer id,
                             @Valid @ModelAttribute TeamViewModel teamViewModel,
                             BindingResult bindingResult,
                             Model model) {
        Team oldTeam = teamService.getById(id);
        if (oldTeam == null) {
            log.warn("Team with id " + id + " not found");
            return "redirect:/teams";
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when updating team with id " + id);
            return "teams/edit-team";
        }

        Team team = mapToTeam(teamViewModel);
        team.setId(id);
        teamService.update(team);
        log.info("Updated team with id " + id);
        return "redirect:/teams";
    }

    private TeamViewModel mapToViewModel(Team team) {
        TeamViewModel viewModel = new TeamViewModel();
        viewModel.setId(team.getId());
        viewModel.setName(team.getName());
        viewModel.setFoundedYear(team.getFoundedYear());
        viewModel.setLeague(team.getLeague() != null ? team.getLeague().name() : "");
        viewModel.setTeamLogoUrl(team.getTeamLogoUrl());
        viewModel.setBudgetInMillions(team.getBudgetInMillions());

        List<String> driverNames = driverService.findByTeamId(team.getId()).stream()
                .map(Driver::getName)
                .collect(Collectors.toList());
        viewModel.setDriverNames(driverNames);

        return viewModel;
    }

    private Team mapToTeam(TeamViewModel viewModel) {
        Team team = new Team();
        team.setId(viewModel.getId());
        team.setName(viewModel.getName());
        team.setFoundedYear(viewModel.getFoundedYear());
        if (viewModel.getLeague() != null && !viewModel.getLeague().isEmpty()) {
            team.setLeague(League.valueOf(viewModel.getLeague()));
        }
        team.setTeamLogoUrl(viewModel.getTeamLogoUrl());
        team.setBudgetInMillions(viewModel.getBudgetInMillions());
        return team;
    }

    @PostMapping("/{id}/delete")
    public String deleteTeam(@PathVariable Integer id, HttpSession session) {
        teamService.delete(id);
        log.info("Deleted team with id " + id + " for session: " + session.getId());
        return "redirect:/teams";
    }

}
