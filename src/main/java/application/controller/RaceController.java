package application.controller;

import application.domain.Driver;
import application.domain.Race;
import application.domain.Track;
import application.service.IDriverService;
import application.service.IRaceService;
import application.service.ITrackService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/races")
public class RaceController {

    private final IRaceService raceService;
    private final IDriverService driverService;
    private final ITrackService trackService;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public RaceController(IRaceService raceService, IDriverService driverService, ITrackService trackService) {
        this.raceService = raceService;
        this.driverService = driverService;
        this.trackService = trackService;
    }


    @GetMapping
    public String showAllRaces(HttpSession session, Model model) {

        model.addAttribute("races", raceService.getAll());
        log.info("Showing all races");
        return "races/races";
    }

    @GetMapping("/{id}")
    public String showRace(@PathVariable Integer id, HttpSession session, Model model) {
        Race race = raceService.getById(id);
        if (race == null) {
            return "redirect:/races";
        }


        model.addAttribute("race", race);
        log.info("Showing race with id " + id);
        return "races/race";
    }

    @GetMapping("/add")
    public String showAddRaceForm(HttpSession session, Model model) {

        model.addAttribute("tracks", trackService.getAll());
        model.addAttribute("drivers", driverService.getAll());
        log.info("Show Add Race Form");
        return "races/add-race";
    }

    @PostMapping("/add")
    public String addRace(@RequestParam String name,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam Integer trackId,
                          @RequestParam(required = false) Integer[] participatingDriverIds,
                          @RequestParam(required = false) Integer winnerId) {

        raceService.addRace(name, date, trackId, participatingDriverIds, winnerId);
        return "redirect:/races";
    }


    @GetMapping("/edit/{id}")
    public String showEditRaceForm(@PathVariable Integer id, HttpSession session, Model model) {
        Race race = raceService.getById(id);
        if (race == null) {
            return "redirect:/races";
        }

        Map<Integer, Integer> driverPositionsMap = new HashMap<>();
        race.getRaceDrivers().forEach(rd -> {
            if (rd.getPosition() != null) {
                driverPositionsMap.put(rd.getDriver().getId(), rd.getPosition());
            }
        });

        model.addAttribute("race", race);
        model.addAttribute("tracks", trackService.getAll());
        model.addAttribute("drivers", driverService.getAll());
        model.addAttribute("driverPositionsMap", driverPositionsMap);
        model.addAttribute("newDrivers", new ArrayList<Driver>());
        model.addAttribute("newTrack", new Track());
        log.info("Show Edit Race Form");
        return "races/edit-race";
    }

    @PostMapping("/edit/{id}")
    public String updateRace(@PathVariable Integer id,
                             @ModelAttribute Race updatedRace,
                             @RequestParam(required = false) Integer[] participatingDriverIds,
                             @RequestParam(required = false) Integer winnerId,
                             @RequestParam Map<String, String> allParams) {

        Race existingRace = raceService.getById(id);
        if (existingRace == null) {
            return "redirect:/races";
        }

        // Extract position data from form parameters (format: driverPosition_{driverId})
        Map<Integer, Integer> driverPositions = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("driverPosition_") && value != null && !value.isEmpty()) {
                try {
                    Integer driverId = Integer.parseInt(key.substring(15)); // "driverPosition_".length() = 15
                    Integer position = Integer.parseInt(value);
                    driverPositions.put(driverId, position);
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                }
            }
        });

        raceService.updateRace(updatedRace, participatingDriverIds, winnerId, existingRace, driverPositions);
        log.info("Updated Race with id " + id);
        return "redirect:/races";
    }


    @PostMapping("/{raceId}/remove-driver/{driverId}")
    public String removeDriverPost(@PathVariable Integer raceId, @PathVariable Integer driverId) {
        Race race = raceService.getById(raceId);
        Driver driver = driverService.getById(driverId);

        if (race == null || driver == null) {
            return "redirect:/races";
        }

        race.removeDriver(driver);
        raceService.update(race);
        log.info("Removed Driver with id " + driverId);
        return "redirect:/races/edit/" + raceId;
    }


    @GetMapping("/{raceId}/add-driver/{driverId}")
    public String addDriverToRace(@PathVariable Integer raceId, @PathVariable Integer driverId, int position, HttpSession session) {
        Race race = raceService.getById(raceId);
        Driver driver = driverService.getById(driverId);

        if (race == null || driver == null) {
            return "redirect:/races";
        }

        raceService.addDriverToRace(position, race, driver);
        log.info("Added Driver with id " + driverId + " for session: " + session.getId());
        return "redirect:/races/edit/" + raceId;
    }

    @PostMapping("/{id}/delete")
    public String deleteRace(@PathVariable Integer id, HttpSession session) {
        raceService.delete(id);
        log.info("Deleted race with id " + id + " for session: " + session.getId());
        return "redirect:/races";
    }

    @GetMapping("/upcoming")
    public String showUpcomingRaces(HttpSession session, Model model) {

        List<Race> upcomingRaces = raceService.findUpcomingRaces();

        model.addAttribute("races", upcomingRaces);
        log.info("Showing upcoming races");
        return "races/upcoming-races";
    }
}
