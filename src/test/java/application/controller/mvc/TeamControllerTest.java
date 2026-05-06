package application.controller.mvc;

import application.TestHelper;
import application.controller.viewmodel.DriverViewModel;
import application.controller.viewmodel.TeamViewModel;
import application.domain.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestHelper testHelper;

    private Team team;

    @BeforeEach
    public void setup() {
        team = testHelper.team("Levski");
        testHelper.user("Levski-manager", team);
    }

    @Test
    @WithUserDetails(value = "Levski-manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getTeamWithDrivers() throws Exception {
        testHelper.driver("Dani", team);

        ResultActions result = mockMvc.perform(get("/teams/{id}", team.getId()));

        result.andExpect(status().isOk())
                .andExpect(view().name("teams/team"))
                .andExpect(model().attributeExists("team"));

        Map<String, Object> model = result.andReturn().getModelAndView().getModel();

        List<DriverViewModel> driversViewModel = (List<DriverViewModel>) model.get("drivers");
        TeamViewModel teamViewModel = (TeamViewModel) model.get("team");

        assertEquals(1, driversViewModel.size());
        assertEquals("Dani", driversViewModel.getFirst().getName());
        assertEquals("Levski", teamViewModel.getName());
    }


    @AfterEach
    public void cleanUp() {
        testHelper.cleanUp();
    }
}
