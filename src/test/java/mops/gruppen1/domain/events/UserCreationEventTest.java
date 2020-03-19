package mops.gruppen1.domain.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreationEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test UserCreationEvent")
    @Test
    void execute() {
        //arrange
        UserCreationEvent userCreationEvent = new UserCreationEvent("Ich-bin-der-Neue");

        //act
        userCreationEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.users.get("Ich-bin-der-Neue")).isNotNull();
        assertThat(testSetup.userToMembers.get("Ich-bin-der-Neue")).isNotNull();
    }
}