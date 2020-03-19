package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ForumCreationEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test ForumCreationEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        ForumCreationEvent forumCreationEvent = new ForumCreationEvent( groupId,
                 "http://www.hierkommstduzudeinemforum.de",
                "VonWemhabenwadas");

        //act
        forumCreationEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getForum()).isNotNull();
        assertThat(groupOne.getForum().getLink()).isEqualTo("http://www.hierkommstduzudeinemforum.de");

    }
}