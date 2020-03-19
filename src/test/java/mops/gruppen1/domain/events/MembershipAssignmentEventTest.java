package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class MembershipAssignmentEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test MembershipAssignmentEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        Username username = new Username("Hand-Test");
        User newUser = new User(username);
        testSetup.users.put(username.toString(), newUser);
        testSetup.userToMembers.put(username.toString(), new ArrayList<>());
        Type membershipType = Type.VIEWER;

        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupId,username.toString(),membershipType.toString());

        //act
        membershipAssignmentEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getMembers().stream().filter(membership -> membership.getUser().equals(newUser)));
    }
}
