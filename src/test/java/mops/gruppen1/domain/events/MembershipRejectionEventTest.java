package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipRejectionEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Test
    @Tag("EventTest")
    @DisplayName("Test MembershipRejectionEvent")
    void execute() {
        //Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();
        String user = testSetup.users.get("Milten").getUsername().getUsername();
        MembershipRejectionEvent membershipRejectionEvent = new MembershipRejectionEvent(groupId, user);

        //Act
        membershipRejectionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //Assert
        Status userStatus = testSetup.memberships.get(5).getStatus();
        assertThat(userStatus).isEqualTo(Status.REJECTED);
    }
}