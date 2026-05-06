package application.controller;

import application.controller.helper.RaceFormFactory;
import application.controller.viewmodel.RaceViewModel;
import application.domain.Race;
import application.mapper.RaceMapper;
import application.service.IRaceService;
import application.service.ITrackService;
import application.controller.viewmodel.AddRaceViewModel;
import application.controller.viewmodel.EditRaceViewModel;
import application.service.command.UpdateRaceCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/races")
public class RaceController {

    private final IRaceService raceService;
    private final ITrackService trackService;
    private final Log log = LogFactory.getLog(this.getClass());
    private final RaceMapper raceMapper;
    private final RaceFormFactory raceFormFactory;

    @Autowired
    public RaceController(IRaceService raceService,
                          ITrackService trackService,
                          RaceMapper raceMapper,
                          RaceFormFactory raceFormFactory) {
        this.raceService = raceService;
        this.trackService = trackService;
        this.raceMapper = raceMapper;
        this.raceFormFactory = raceFormFactory;
    }

    @GetMapping
    public String showAllRaces(Model model) {
        model.addAttribute("races", raceService.getAllRaces());
        log.info("Showing all races");
        return "races/races";
    }

    @GetMapping("/{id}")
    public String showRace(@PathVariable Integer id , Model model) {
        model.addAttribute("race", raceService.getById(id));
        log.info("Showing race with id " + id);
        return "races/race";
    }

    @GetMapping("/add")
    public String showAddRaceForm(Model model) {
        model.addAttribute("addRaceViewModel", raceFormFactory.buildAddRaceForm());
        model.addAttribute("tracks", trackService.getAll());
        log.info("Show Add Race Form");
        return "races/add-race";
    }

    @PostMapping("/add")
    public String addRace(@ModelAttribute AddRaceViewModel addRaceViewModel) {
        raceService.addRace(raceMapper.toAddRaceCommand(addRaceViewModel));
        return "redirect:/races";
    }

    @GetMapping("/edit/{id}")
    public String showEditRaceForm(@PathVariable Integer id, Model model) {
        EditRaceViewModel editRaceViewModel = raceFormFactory.buildEditRaceForm(id);

        model.addAttribute("editRaceViewModel", editRaceViewModel);
        model.addAttribute("tracks", trackService.getAll());
        log.info("Show Edit Race Form");
        return "races/edit-race";
    }

    @PostMapping("/edit/{id}")
    public String updateRace(@PathVariable Integer id, @ModelAttribute EditRaceViewModel editRaceViewModel) {
        UpdateRaceCommand updateRaceCommand = raceMapper.toUpdateRaceCommand(editRaceViewModel);

        updateRaceCommand.setId(id);

        raceService.updateRace(updateRaceCommand);
        log.info("Updated Race with id " + id);
        return "redirect:/races";
    }

    @PostMapping("/{raceId}/remove-driver/{driverId}")
    public String removeDriverPost(@PathVariable Integer raceId, @PathVariable Integer driverId) {
        raceService.removeDriverFromRace(raceId, driverId);
        log.info("Removed Driver with id " + driverId);
        return "redirect:/races/edit/" + raceId;
    }

    @GetMapping("/{raceId}/add-driver/{driverId}")
    public String addDriverToRace(@PathVariable Integer raceId,
                                  @PathVariable Integer driverId,
                                  @RequestParam(required = false) Integer position
                                  ) {
        raceService.addDriverToRace(raceId, driverId, position);
        log.info("Added Driver with id " + driverId);
        return "redirect:/races/edit/" + raceId;
    }

    @PostMapping("/{id}/delete")
    public String deleteRace(@PathVariable Integer id) {
        raceService.delete(id);
        log.info("Deleted race with id " + id);
        return "redirect:/races";
    }

    @GetMapping("/upcoming")
    public String showUpcomingRaces(Model model) {
        List<Race> races = raceService.findUpcomingRaces();
        List<RaceViewModel> upcomingRaces = raceMapper.toRaceViewModelList(races);
        model.addAttribute("races", upcomingRaces);
        log.info("Showing upcoming races");
        return "races/upcoming-races";
    }
}
