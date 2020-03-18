package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDeletionEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {

        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in group-Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroups() {
        //arrange
        String testGroupId = testSetup.groupOne.getGroupId().toString();
        String removedMemberId = testSetup.memberships.get(1).getMemberid().toString();
        String removedByMemberId = testSetup.memberships.get(0).getMemberid().toString();
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(testGroupId, removedMemberId, removedByMemberId);
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getStatus()
                .equals((Status.DEACTIVATED))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - Deaktivierung in groupToMembers - Hashmap.")
    @Test
    void testExecuteDeactivateMembershipsInGroupToMembers() {
        //arrange
        Group testGroup = testSetup.groupOne;
        String testGroupId = testGroup.getGroupId().toString();
        String removedMemberId = testSetup.memberships.get(1).getMemberid().toString();
        String removedByMemberId = testSetup.memberships.get(0).getMemberid().toString();
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(testGroupId, removedMemberId, removedByMemberId);
        List<Membership> testMemberlist = testSetup.groupToMembers.get(testGroup);

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getStatus()
                .equals((Status.DEACTIVATED))).isEqualTo(true);
    }
}
