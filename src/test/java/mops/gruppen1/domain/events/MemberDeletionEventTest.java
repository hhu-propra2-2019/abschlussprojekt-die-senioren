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
    @DisplayName("Teste Member - Deaktivierung.")
    @Test
    void testExecuteDeactivateMemberships() {
        //arrange
        String groupId = testSetup.groupOne.getGroupId().toString();
        String removedMemberId = testSetup.memberships.get(1).getMemberid().toString();
        String removedByMemberId = testSetup.memberships.get(0).getMemberid().toString();
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(groupId, removedMemberId, removedByMemberId);
        List<Membership> testMemberlist = testSetup.groupOne.getMembers();

        //act
        memberDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testMemberlist.get(1).getStatus().equals(Status.DEACTIVATED)).isEqualTo(true);
    }
}
