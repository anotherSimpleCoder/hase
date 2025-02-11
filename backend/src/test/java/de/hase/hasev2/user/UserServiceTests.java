package de.hase.hasev2.user;

import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static de.hase.hasev2.database.Tables.USERS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class UserServiceTests {
    @Autowired
    private UserService userService;
    private DSLContext dslContext;

    private final UserBuilder testUserBuilder = new UserBuilder()
            .firstName("Test")
            .lastName("User")
            .email("test@mail.de")
            .password("test");

    public UserServiceTests(@Autowired HikariService hikariService) throws Exception {
        dslContext = DSL.using(hikariService.getDataSource().getConnection());
    }

    @BeforeEach
    void clearDatabaseAfterEachTest(){
        dslContext.deleteFrom(USERS).execute();
    }

    @Test
    void saveUser_shouldBeOk() throws Exception {
        this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));
    }

    @Test
    void saveUser_shouldBeEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var gottenUser = this.userService.findUser(savedUser.matrikelNr())
                .orElseThrow(() -> new Exception("User could not be found!"));

        assertEquals(savedUser, gottenUser);
    }

    @Test
    void deleteUser_shouldBeOk() throws Exception {
        var savedUser = this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));

        this.userService.deleteUser(savedUser.matrikelNr())
                .orElseThrow(() -> new Exception("User could not be deleted!"));
    }

    @Test
    void deleteUser_shouldBeEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var deletedUser = this.userService.deleteUser(savedUser.matrikelNr())
                    .orElseThrow(() -> new Exception("User could not be deleted!"));

        assertEquals(savedUser, deletedUser);

        assertThrows(Exception.class, () -> {
            this.userService.findUser(savedUser.matrikelNr())
                    .orElseThrow(() -> new Exception("User could not be found!"));
        });
    }

    @Test
    void updateUser_shouldBeOk() throws Exception {
        var savedUser = this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var newUpdated = new User(savedUser.matrikelNr(), "Updated", "Updated", "Updated@mail.com", "Updated");

        this.userService.updateUser(newUpdated)
                .orElseThrow(() -> new Exception("User could not be updated!"));
    }

    @Test
    void updateUser_shouldBeNotEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUserBuilder.build())
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var newUpdated = new User(savedUser.matrikelNr(), "Updated", "Updated", "Updated@mail.com", "Updated");

        var updatedUser = this.userService.updateUser(newUpdated)
                .orElseThrow(() -> new Exception("User could not be updated!"));

        assertNotEquals(savedUser, updatedUser);
    }

    @Test
    void saveInvalidUser_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.userService.saveUser(null);
        });
    }

    @Test
    void getInvalidUser_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.userService.findUser(-1)
                    .orElseThrow(() -> new Exception("User could not be found!"));
        });
    }

    @Test
    void deleteInvalidUser_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.userService.deleteUser(-1)
                    .orElseThrow(() -> new Exception("User could not be found!"));
        });
    }

    @Test
    void updateInvalidUser_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.userService.updateUser(null)
                    .orElseThrow(() -> new Exception("User could not be updated!"));
        });
    }
}
