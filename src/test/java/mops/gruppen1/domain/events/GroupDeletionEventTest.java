package mops.gruppen1.domain.events;

import mops.gruppen1.domain.GroupStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupDeletionEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Test
    @Tag("EventTest")
    @DisplayName("Test GroupeDeletionEvent")
    void testExecute() {
        //Arrange
        String groupId = testSetup.groupOne.getGroupId().toString();
        String deletedBy = "Max";
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, deletedBy);

        //Act
        groupDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //Assert
        assertThat(testSetup.groupOne.getGroupStatus()).isEqualTo(GroupStatus.DEACTIVATED);
    }
}
