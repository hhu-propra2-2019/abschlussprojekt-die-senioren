package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberResignmentEventTest {

    private TestSetup testSetup;
    private String testGroupId;
    private String leavingUserID;
    private MemberResignmentEvent memberResignmentEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.leavingUserID = testSetup.groupOne.getMembers().get(1).getUser().getUsername().getUsername();
        this.memberResignmentEvent = new MemberResignmentEvent(testGroupId, leavingUserID);
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
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getMembershipStatus()
                .equals((MembershipStatus.DEACTIVATED))).isEqualTo(true);
    }


    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in groupToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        String testGroupID = testSetup.groupOne.getGroupId().toString();
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroupID);

        //act
        memberResignmentEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getMembershipStatus()
                .equals((MembershipStatus.DEACTIVATED))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in userToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInUserToMembers() {
        //arrange
        String removedUser = testSetup.memberships.get(1).getUser().getUsername().getUsername();
        List<Membership> testMemberlist = testSetup.userToMembers.get(removedUser);

        //act
        memberResignmentEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        //Da Membership - List von User, hat diese nur einen Eintrag, da jeder User im testSetup nur in genau
        //einer Gruppe ist.
        assertThat(testMemberlist.get(0).getMembershipStatus()
                .equals((MembershipStatus.DEACTIVATED))).isEqualTo(true);
    }
}