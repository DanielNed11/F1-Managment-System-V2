package application.controller.api;

import application.api.ApiDriverController;
import application.api.dto.AddDriverDTO;
import application.api.dto.DriverDTO;
import application.domain.AppUser;
import application.domain.Driver;
import application.domain.Role;
import application.mapper.DriverMapper;
import application.security.CustomUser;
import application.service.IDriverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class ApiDriverControllerUnitTest {

    @Autowired
    private ApiDriverController sut;
    @MockitoBean
    private IDriverService driverService;
    @MockitoBean
    private DriverMapper driverMapper;

    @Test
    void addDriverReturnsCreated() {
        // Arrange
        AddDriverDTO dto = new AddDriverDTO();
        dto.setName("Dani");
        dto.setNationality("Bulgarian");
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setNationality(dto.getNationality());
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setName(dto.getName());
        driverDTO.setNationality(dto.getNationality());

        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setUsername("test");
        appUser.setPassword("test");
        appUser.setRole(Role.USER);
        CustomUser customUser = CustomUser.buildUser(appUser);

        given(driverService.add(driver, appUser.getId()))
                .willAnswer(invocation -> {
                    driver.setId(1);
                    return driver;
                });
        given(driverMapper.toDriver(dto)).willReturn(driver);
        given(driverMapper.toDriverDTO(driver)).willReturn(driverDTO);

        // Act
        ResponseEntity<DriverDTO> response = sut.addDriver(dto, customUser);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(driverDTO, response.getBody());

        verify(driverMapper).toDriver(dto);
        verify(driverMapper).toDriverDTO(driver);
        verify(driverService).add(driver, appUser.getId());
    }

    @Test
    void addDriverPropagatesServiceException() {
        // Arrange
        AddDriverDTO dto = new AddDriverDTO();
        dto.setName("Dani");
        dto.setNationality("Bulgarian");
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setNationality(dto.getNationality());

        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setUsername("test");
        appUser.setPassword("test");
        appUser.setRole(Role.USER);
        CustomUser customUser = CustomUser.buildUser(appUser);

        given(driverService.add(driver, appUser.getId()))
                .willThrow(NoSuchElementException.class);
        given(driverMapper.toDriver(dto)).willReturn(driver);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> sut.addDriver(dto, customUser));

        verify(driverMapper).toDriver(dto);
        verify(driverService).add(driver, appUser.getId());
    }
}
