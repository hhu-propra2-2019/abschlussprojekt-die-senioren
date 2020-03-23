package mops.gruppen1.applicationService;

import mops.gruppen1.domain.User;
import mops.gruppen1.domain.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class CheckServiceTest {

    CheckService checkService;

    @BeforeEach
    public void setUp() {
        this.checkService = new CheckService();
    }

    @Tag("CheckServiceTest")
    @Test
    void testDoesUserExistPositive() {
        //Arrange

        String userName1 = "Test User1";
        String userName2 = "Test User2";
        User user1 = new User(new Username(userName1));
        HashMap<String, User> users = new HashMap<>();
        users.put(userName1, user1);

        //act
        ValidationResult validationResult = checkService.doesUserExist(userName2, users);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("CheckServiceTest")
    @Test
    void testDoesUserExistFalse() {
        //Arrange

        String userName1 = "Test User1";
        String userName2 = "Test User2";
        User user1 = new User(new Username(userName1));
        User user2 = new User(new Username(userName2));
        HashMap<String, User> users = new HashMap<>();
        users.put(userName1, user1);
        users.put(userName2, user2);

        //act
        ValidationResult validationResult = checkService.doesUserExist(userName2, users);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

}