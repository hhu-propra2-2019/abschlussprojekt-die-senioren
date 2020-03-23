package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDeletionEventTest {

    private TestSetup testSetup;
    private String testGroupId;
    private String removedMemberId;
    private String removedByMemberId;
    private MemberDeletionEvent memberDeletionEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.removedMemberId = testSetup.memberships.get(1).getMemberid().toString();
        this.removedByMemberId = testSetup.memberships.get(0).getMemberid().toString();
        this.memberDeletionEvent = new MemberDeletionEvent(testGroupId, removedMemberId, removedByMemberId);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in group-Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroups() {
        //arrange
        List<Membership> testMemberlist = testSetup.groups.get(testGroupId).getMembers();

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getStatus()
                .equals((MembershipStatus.DEACTIVATED))).isTrue();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in groupToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        String testGroupID = testSetup.groupOne.getGroupId().toString();
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroupID);

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getStatus()
                .equals((MembershipStatus.DEACTIVATED))).isTrue();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in userToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInUserToMembers() {
        //arrange
        String removedUser = testSetup.memberships.get(1).getUser().getUsername().getUsername();
        List<Membership> testMemberlist = testSetup.userToMembers.get(removedUser);

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        //Da Membership - List von User, hat diese nur einen Eintrag, da jeder User im testSetup nur in genau
        //einer Gruppe ist.
        assertThat(testMemberlist.get(0).getStatus()
                .equals((MembershipStatus.DEACTIVATED))).isTrue();
    }
}
