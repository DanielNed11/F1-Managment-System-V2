package application.controller;

import application.session.SessionHistory;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SessionHistoryController {

    private final SessionHistory sessionHistory;
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public SessionHistoryController(SessionHistory sessionHistory) {
        this.sessionHistory = sessionHistory;
    }

    @GetMapping("/session-history")
    public String showSessionHistory(HttpSession session, Model model) {
        model.addAttribute("visits", sessionHistory.getVisits(session));
        model.addAttribute("visitCount", sessionHistory.getVisitCount(session));
        model.addAttribute("sessionId", session.getId());
        log.info("Displaying session history with " + sessionHistory.getVisitCount(session) + " visits for session " + session.getId());
        return "session-history";
    }

    @PostMapping("/session-history/clear")
    public String clearSessionHistory(HttpSession session) {
        sessionHistory.clearHistory(session);
        log.info("Session history cleared for session " + session.getId());
        return "redirect:/session-history";
    }
}
