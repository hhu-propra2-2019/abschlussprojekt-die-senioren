package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaterialCreationEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test MaterialCreationEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        MaterialCreationEvent materialCreationEvent = new MaterialCreationEvent( groupId,
                 "http://www.hierkommstduzudeinenmaterialien.de",
                "VonWemhabenwadas");

        //act
        materialCreationEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getMaterial()).isNotNull();
        assertThat(groupOne.getMaterial().getLink()).isEqualTo("http://www.hierkommstduzudeinenmaterialien.de");

    }
}