package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberResignmentEventTest {

    private TestSetup testSetup;
    private String testGroupId;
    private String leavingMemberID;
    private MemberResignmentEvent memberResignmentEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.leavingMemberID = testSetup.memberships.get(1).getMemberid().toString();
        this.memberResignmentEvent = new MemberResignmentEvent(testGroupId, leavingMemberID);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in group-Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroups() {
        //arrange
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();

        //act
        memberResignmentEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getStatus()
                .equals((Status.DEACTIVATED))).isEqualTo(true);
    }


}