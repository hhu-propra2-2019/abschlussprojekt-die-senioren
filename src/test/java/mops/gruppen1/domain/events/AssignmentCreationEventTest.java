package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Assignment;
import mops.gruppen1.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssignmentCreationEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test AssignmentCreationEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        AssignmentCreationEvent assignmentCreationEvent = new AssignmentCreationEvent( groupId,
                 "http://www.hierkommstduzudeinenabgaben.de",
                "VonWemhabenwadas");

        //act
        assignmentCreationEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getAssignment()).isNotNull();
        assertThat(groupOne.getAssignment().getLink()).isEqualTo("http://www.hierkommstduzudeinenabgaben.de");

    }
}