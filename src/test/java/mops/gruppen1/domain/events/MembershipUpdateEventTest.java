package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipUpdateEventTest {

    private final String updatedToAdmin = "ADMIN";
    private final String updatedToViewer = "VIEWER";
    private TestSetup testSetup;
    private String testGroupId;
    private String updatedMember;
    private String updatedBy;
    private MembershipUpdateEvent membershipUpdateEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.updatedMember = testSetup.memberships.get(1).getMemberid().toString();
        this.updatedBy = testSetup.memberships.get(0).getMemberid().toString();
        this.membershipUpdateEvent = new MembershipUpdateEvent(testGroupId, updatedMember, updatedBy, updatedToAdmin);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Type Änderung von Viewer zu Admin in group-Hashmap.")
    @Test
    void testExecuteChangeTypeInGroups() {
        //arrange
        List<Membership> testMemberlist = testSetup.groups.get(testGroupId).getMembers();

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getType()
                .equals((Type.ADMIN))).isTrue();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Type Änderung von Viewer zu Admin in groupToMembers-Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        String testGroupID = testSetup.groupOne.getGroupId().toString();
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroupID);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getType()
                .equals((Type.ADMIN))).isTrue();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Type Änderung von Viewer zu Admin in userToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInUserToMembers() {
        //arrange
        String removedUser = testSetup.memberships.get(1).getUser().getUsername().getUsername();
        List<Membership> testMemberlist = testSetup.userToMembers.get(removedUser);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        //Da Membership - List von User, hat diese nur einen Eintrag, da jeder User im testSetup nur in genau
        //einer Gruppe ist.
        assertThat(testMemberlist.get(0).getType()
                .equals((Type.ADMIN))).isTrue();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Type Änderung von Admin zu Viewer in group-Hashmap.")
    @Test
    void testExecuteChangeTypeInGroupsAdminToViewer() {
        //arrange
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();
        String adminMember = testSetup.memberships.get(0).getMemberid().toString();
        String updatedBy2 = testSetup.memberships.get(1).getMemberid().toString();
        MembershipUpdateEvent membershipUpdateEvent2 = new MembershipUpdateEvent(testGroupId, adminMember,
                updatedBy2, updatedToViewer);

        //act
        //First: Change viewer to admin who will after that change antoher admin to viewer.
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users,
                testSetup.groups);
        membershipUpdateEvent2.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users,
                testSetup.groups);

        //assert
        assertThat(testSetup.groupOne.getMembers().get(0).getType()
                .equals((Type.VIEWER))).isTrue();
    }
}