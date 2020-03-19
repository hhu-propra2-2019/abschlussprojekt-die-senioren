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
    @DisplayName("Teste Member - Type Änderung in group-Hashmap.")
    @Test
    void testExecuteChangeTypeInGroups() {
        //arrange
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getType()
                .equals((Type.ADMIN))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Type Änderung in groupToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        String testGroupID = testSetup.groupOne.getGroupId().toString();
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroupID);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getType()
                .equals((Type.ADMIN))).isEqualTo(true);
    }
}