package de.hase.hasev2.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    private final User testUser = new User(0, "Test", "User", "test@mail.de", "test");

    @Test
    void saveUser_shouldBeOk() throws Exception {
        this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("User could not be saved!"));
    }

    @Test
    void saveUser_shouldBeEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var gottenUser = this.userService.findUser(savedUser.matrikelNr())
                .orElseThrow(() -> new Exception("User could not be found!"));

        assertEquals(savedUser, gottenUser);
    }

    @Test
    void deleteUser_shouldBeOk() throws Exception {
        var savedUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("User could not be saved!"));

        this.userService.deleteUser(savedUser.matrikelNr())
                .orElseThrow(() -> new Exception("User could not be deleted!"));
    }

    @Test
    void deleteUser_shouldBeEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUser)
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
        var savedUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var newUpdated = new User(savedUser.matrikelNr(), "Updated", "Updated", "Updated", "Updated");

        this.userService.updateUser(newUpdated)
                .orElseThrow(() -> new Exception("User could not be updated!"));
    }

    @Test
    void updateUser_shouldBeNotEqual() throws Exception {
        var savedUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("User could not be saved!"));

        var newUpdated = new User(savedUser.matrikelNr(), "Updated", "Updated", "Updated", "Updated");

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
