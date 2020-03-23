package mops.gruppen1.domain.events;

import mops.gruppen1.domain.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipUpdateEventTest {

    private final String updatedTo1 = "ADMIN";
    private final String updatedTo2 = "VIEWER";
    private TestSetup testSetup;
    private String testGroupId;
    private String updatedUser;
    private String updatedBy;
    private MembershipUpdateEvent membershipUpdateEvent;

    @BeforeEach
    void setup() {
        //allgemeiner arrange - Schritt
        this.testSetup = new TestSetup();
        this.testGroupId = testSetup.groupOne.getGroupId().toString();
        this.updatedBy = testSetup.groupOne.getMembers().get(0).getUser().getUsername().getUsername();
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Viewer zu Admin in group-Hashmap.")
    @Test
    void testChangeTypeToAdmin() {
        //arrange
        updatedUser = testSetup.groupOne.getMembers().get(1).getUser().getUsername().getUsername();
        membershipUpdateEvent = new MembershipUpdateEvent(testGroupId, updatedUser, updatedBy, updatedTo1);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getMembershipType()
                .equals((MembershipType.ADMIN))).isEqualTo(true);
    }

    @Tag("EventTest")
    @DisplayName("Teste Member - MembershipType Änderung von Viewer zu Admin in group-Hashmap.")
    @Test
    void testChangeTypeToViewer() {
        //arrange
        updatedUser = testSetup.groupOne.getMembers().get(1).getUser().getUsername().getUsername();
        membershipUpdateEvent = new MembershipUpdateEvent(testGroupId, updatedUser, updatedBy, updatedTo2);

        //act
        membershipUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(testSetup.groups.get(testGroupId).getMembers().get(1).getMembershipType()
                .equals((MembershipType.VIEWER))).isEqualTo(true);
    }
}