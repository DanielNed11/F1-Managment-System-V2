package application.repository;

import application.domain.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class AppUserRepositoryTest {

    @Autowired
    private IAppUserRepository appUserRepository;

    @Test
    public void duplicateUsernamesIsNotAllowed() {
        // Arrange
        AppUser old = new AppUser();
        old.setUsername("test");
        old.setPassword("test");
        appUserRepository.save(old);

        // Act
        AppUser newUser = new AppUser();
        newUser.setUsername("test");
        newUser.setPassword("test");

        // Assert
        assertThrows(DataIntegrityViolationException.class, () -> appUserRepository.saveAndFlush(newUser));
    }

    @AfterEach
    public void cleanup() {
        appUserRepository.deleteAll();
    }

}

