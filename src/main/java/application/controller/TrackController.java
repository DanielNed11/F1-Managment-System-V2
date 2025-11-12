package application.controller;

import application.domain.Track;
import application.service.impl.TrackService;
import application.session.SessionHistory;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final SessionHistory sessionHistory;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public TrackController(TrackService trackService, SessionHistory sessionHistory) {
        this.trackService = trackService;
        this.sessionHistory = sessionHistory;
    }

    @GetMapping
    public String showAllTracks(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/tracks", "All Tracks");

        model.addAttribute("tracks", trackService.getAll());
        log.info("Showing all tracks");
        return "tracks/tracks";
    }

    @GetMapping("/{id}")
    public String getTeam(HttpSession session, Model model, @PathVariable int id) {
        sessionHistory.addVisit(session, "/tracks/" + id, "Track Details");

        model.addAttribute("tracks", trackService.getById(id));
        log.info("Getting track with id " + id);
        return "tracks/tracks";
    }

    @GetMapping("/add")
    public String showAddTrack(HttpSession session, Model model) {
        sessionHistory.addVisit(session, "/tracks/add", "Add Track");

        model.addAttribute("track", new Track());
        log.info("Showing add track form");
        return "tracks/add-track";
    }

    @PostMapping("/add")
    public String addTrack(@ModelAttribute Track track) {
        trackService.add(track);
        return "redirect:/tracks";
    }

    @GetMapping("/edit/{id}")
    public String showEditTrackForm(HttpSession session, Model model, @PathVariable int id) {
        sessionHistory.addVisit(session, "/tracks/edit/" + id, "Edit Track");

        model.addAttribute("track", trackService.getById(id));
        log.info("Editing track with id " + id);
        return "tracks/edit-track";
    }

    @PostMapping("/edit/{id}")
    public String editTrack(@PathVariable int id, @ModelAttribute Track track) {
        Track oldTrack = trackService.getById(id);
        if (oldTrack == null) {
            log.info("Track with id " + id + " not found");
            return "redirect:/tracks";
        }
        log.info("Edited track with id " + id);
        trackService.update(track);
        return "redirect:/tracks";
    }

}
