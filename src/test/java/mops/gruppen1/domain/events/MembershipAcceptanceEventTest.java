package mops.gruppen1.domain.events;

import mops.gruppen1.domain.MembershipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipAcceptanceEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Test
    @Tag("EventTest")
    @DisplayName("Test MembershipAcceptanceEvent")
    void execute() {
        //Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();
        String user = "Milten";
        String acceptedBy = "Diego";
        MembershipAcceptanceEvent membershipAcceptanceEvent = new MembershipAcceptanceEvent(groupId, user, acceptedBy);

        //Act
        membershipAcceptanceEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //Assert
        MembershipStatus userMembershipStatus = testSetup.memberships.get(5).getMembershipStatus();
        assertThat(userMembershipStatus).isEqualTo(MembershipStatus.ACTIVE);
    }
}