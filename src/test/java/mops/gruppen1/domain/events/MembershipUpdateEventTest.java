package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipUpdateEventTest {

    private TestSetup testSetup;
    private String testGroupId;
    private String updatedMember;
    private String updatedBy;
    private final String updatedTo1 = "ADMIN";
    private final String updatedTo2= "VIEWER";
    private MembershipUpdateEvent membershipUpdateEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.updatedMember = testSetup.memberships.get(1).getMemberid().toString();
        this.updatedBy = testSetup.memberships.get(0).getMemberid().toString();
        this.membershipUpdateEvent = new MembershipUpdateEvent(testGroupId, updatedMember,updatedBy,updatedTo1);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Viewer zu Admin in group-Hashmap.")
    @Test
    void testExecuteChangeTypeInGroups() {
        //arrange
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getMembershipType()
                .equals((MembershipType.ADMIN))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Viewer zu Admin in groupToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        String testGroupID = testSetup.groupOne.getGroupId().toString();
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroupID);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getMembershipType()
                .equals((MembershipType.ADMIN))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Viewer zu Admin in userToMembers - Hashmap.")
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
        assertThat(testMemberlist.get(0).getMembershipType()
                .equals((MembershipType.ADMIN))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Admin zu Viewer in group-Hashmap.")
    @Test
    void testExecuteChangeTypeInGroupsAdminToViewer() {
        //arrange
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();
        String updatedMember2 = testSetup.memberships.get(0).getMemberid().toString();
        String updatedBy2 = testSetup.memberships.get(1).getMemberid().toString();
        MembershipUpdateEvent membershipUpdateEvent2 = new MembershipUpdateEvent(testGroupId, updatedMember2,
                updatedBy2,updatedTo2);

        //act
        //First: Change viewer to admin who will after that change the antoher admin to viewer.
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users,
                testSetup.groups);
        membershipUpdateEvent2.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users,
                testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(0).getMembershipType()
                .equals((MembershipType.VIEWER))).isEqualTo(true);
    }
}