package application.controller.api;

import application.TestHelper;
import application.domain.Driver;
import application.domain.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiDriverControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestHelper testHelper;

    private Team team;

    @BeforeEach
    public void setUp() {
        team = testHelper.team("Levski");
        testHelper.user("Levski-manager", team);
        testHelper.admin("admin", team);
    }

    @Test
    @WithUserDetails(value = "Levski-manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void managerCanPatchDriverFromManagedTeam() throws Exception {
        Driver driver = testHelper.driver("Dani", team);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Ivan",
                                "nationality": "Bulgaria",
                                "dateOfBirth": null,
                                "worldChampionships": 5,
                                "imageUrl": null,
                                "teamId": null
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.nationality").value("Bulgaria"))
                .andExpect(jsonPath("$.worldChampionships").value(5))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.simpleTeamDTO.id").value(team.getId()))
                .andExpect(jsonPath("$.simpleTeamDTO.name").value("Levski"));

    }

    @Test
    @WithUserDetails(value = "Levski-manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void managerCannotPatchDriverFromDifferentTeam() throws Exception {
        Team newTeam = testHelper.team("CSKA");
        Driver driver = testHelper.driver("Ivan", newTeam);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Dani",
                                "nationality": "Bulgaria",
                                "dateOfBirth": null,
                                "worldChampionships": 5,
                                "imageUrl": null,
                                "teamId": null
                                }
                                """))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void adminCanPatchDriverFromAnyTeamAndChangeTeams() throws Exception {
        Team newTeam = testHelper.team("CSKA");
        Driver driver = testHelper.driver("Ivan", newTeam);
        Integer levskiId = team.getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Dani",
                                "nationality": "Bulgaria",
                                "dateOfBirth": null,
                                "worldChampionships": 5,
                                "imageUrl": null,
                                "teamId": %d
                                }
                                """.formatted(levskiId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Dani"))
                .andExpect(jsonPath("$.nationality").value("Bulgaria"))
                .andExpect(jsonPath("$.worldChampionships").value(5))
                .andExpect(jsonPath("$.simpleTeamDTO.id").value(levskiId))
                .andExpect(jsonPath("$.simpleTeamDTO.name").value("Levski"));
    }

    @Test
    @WithUserDetails(value = "Levski-manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void managerCanDeleteDriverFromManagedTeam() throws Exception {
        Driver driver = testHelper.driver("Dani", team);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "Levski-manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void managerCannotDeleteDriverFromDifferentTeam() throws Exception {
        Team newTeam = testHelper.team("CSKA");
        Driver driver = testHelper.driver("Ivan", newTeam);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails(value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void adminCanDeleteDriverFromAnyTeam() throws Exception {
        Team newTeam = testHelper.team("CSKA");
        Driver driver = testHelper.driver("Ivan", newTeam);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/{id}", driver.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @AfterEach
    public void cleanUp() {
        testHelper.cleanUp();
    }
}
