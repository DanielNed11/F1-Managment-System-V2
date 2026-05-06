package application.controller.mvc;

import application.TestHelper;
import application.controller.viewmodel.DriverViewModel;
import application.domain.Driver;
import application.repository.IDriverRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private IDriverRepository iDriverRepository;


    @Test
    void showAllDriversFiltersByNationality() throws Exception {
        Driver driver = new Driver();
        driver.setNationality("Bulgarian");
        iDriverRepository.save(driver);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/drivers?nationality=Bulgarian"));

        result.andExpect(status().isOk())
                .andExpect(view().name("drivers/drivers"))
                .andExpect(model().attribute("drivers", hasSize(1)));

        Map<String, Object> model = result.andReturn().getModelAndView().getModel();

        List<DriverViewModel> driverViewModels = (List<DriverViewModel>) model.get("drivers");

        assertEquals("Bulgarian", driverViewModels.getFirst().getNationality());
        assertEquals(driver.getId(), driverViewModels.getFirst().getId());

    }

    @AfterEach
    public void cleanUp() {
        testHelper.cleanUp();
    }

}
