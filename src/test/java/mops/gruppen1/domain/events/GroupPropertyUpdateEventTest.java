package mops.gruppen1.domain.events;

import mops.gruppen1.domain.GroupType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupPropertyUpdateEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Test
    @Tag("EventTest")
    @DisplayName("Test PropertyUpdateEvent")
    void testExecute() {
        //Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();
        String userName = "Diego";
        String groupName = "das neue Lage Deluxe";
        String description = "Sumpflager ist eh besser";
        String groupType = "PUBLIC";
        GroupPropertyUpdateEvent groupPropertyUpdateEvent = new GroupPropertyUpdateEvent(groupId, userName, groupName, description, groupType);

        //Act
        groupPropertyUpdateEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //Assert
        String newGroupName = testSetup.groupThree.getName().toString();
        String newDescription = testSetup.groupThree.getDescription().toString();
        assertThat(newGroupName).isEqualTo(groupName);
        assertThat(newDescription).isEqualTo(description);
        assertThat(testSetup.groupThree.getGroupType()).isEqualTo(GroupType.PUBLIC);

    }
}