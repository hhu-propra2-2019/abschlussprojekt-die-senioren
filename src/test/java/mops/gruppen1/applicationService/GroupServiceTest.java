package mops.gruppen1.applicationService;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.events.TestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GroupServiceTest {

    GroupService groupService;
    TestSetup testSetup;

    @BeforeEach
    public void setUp() {
        testSetup = new TestSetup();
        EventService eventServiceMock = mock(EventService.class);
        CheckService checkServiceMock = mock(CheckService.class);
        this.groupService = new GroupService(
                eventServiceMock,
                checkServiceMock,
                testSetup.groupToMembers, testSetup.userToMembers,
                testSetup.groups, testSetup.users,
                testSetup.groupThree.getGroupId().toString()
        );
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipToPublicGroupPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isPublic(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAssignmentEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.assignMembershipToPublicGroup(userName, groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipToPublicGroupFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isPublic(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAssignmentEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.assignMembershipToPublicGroup(userName, groupId, membershipType);
        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipToRestrictedGroupPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAssignmentEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.assignMembershipToRestrictedGroup(userName, groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipToRestrictedGroupFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAssignmentEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.assignMembershipToRestrictedGroup(userName, groupId, membershipType);
        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testAcceptMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String acceptedBy = "Admin";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.isAdmin(acceptedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);


        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAcceptanceEvent(userName, groupId, acceptedBy);

        //act
        ValidationResult validationResult = groupService1.acceptMembership(userName, groupId, acceptedBy);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testAcceptMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String acceptedBy = "Admin";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.isAdmin(acceptedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipAcceptanceEvent(userName, groupId, acceptedBy);

        //act
        ValidationResult validationResult = groupService1.acceptMembership(userName, groupId, acceptedBy);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testRejectMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String rejectedBy = "Test2";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(rejectedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipRejectEvent(userName, groupId, rejectedBy);

        //act
        ValidationResult validationResult = groupService1.rejectMembership(userName, groupId, rejectedBy);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testRejectMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String rejectedBy = "Test2";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(rejectedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipRejectEvent(userName, groupId, rejectedBy);

        //act
        ValidationResult validationResult = groupService1.rejectMembership(userName, groupId, rejectedBy);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testRequestMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipRequestEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.requestMembership(userName, groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testRequestMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isNotMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipRequestEvent(userName, groupId, membershipType);

        //act
        ValidationResult validationResult = groupService1.requestMembership(userName, groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testUpdateMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String updatedBy = "Test2";
        String groupId = "1";
        String updatedTo = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(updatedBy, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipUpdateEvent(userName, groupId, updatedBy);

        //act
        ValidationResult validationResult = groupService1.updateMembership(userName, groupId, updatedBy);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testUpdateMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String updatedBy = "Test2";
        String groupId = "1";
        String updatedTo = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(updatedBy, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipUpdateEvent(userName, groupId, updatedBy);

        //act
        ValidationResult validationResult = groupService1.updateMembership(userName, groupId, updatedBy);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testDeleteMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String deletedBy = "Test2";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(deletedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(deletedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(deletedBy, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipDeletionEvent(userName, groupId, deletedBy);

        //act
        ValidationResult validationResult = groupService1.deleteMembership(userName, groupId, deletedBy);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testDeleteMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String deletedBy = "Test2";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(deletedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(deletedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(deletedBy, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipDeletionEvent(userName, groupId, deletedBy);

        //act
        ValidationResult validationResult = groupService1.deleteMembership(userName, groupId, deletedBy);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testResignMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(userName, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipResignmentEvent(userName, groupId);

        //act
        ValidationResult validationResult = groupService1.resignMembership(userName, groupId);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testResignMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        ValidationResult validationResult4 = new ValidationResult();

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);
        when(groupService.checkService.activeAdminRemains(userName, userName, groupId, groupService.getGroupToMembers()))
                .thenReturn(validationResult4);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performMembershipResignmentEvent(userName, groupId);

        //act
        ValidationResult validationResult = groupService1.resignMembership(userName, groupId);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testCreateUserPositiveChecks() {
        //Arrange
        String userName = "Test";

        ValidationResult validationResult1 = new ValidationResult();

        when(groupService.checkService.doesUserExist(userName, groupService.getUsers())).thenReturn(validationResult1);

        //act
        ValidationResult validationResult = groupService.createUser(userName);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testCreateUserFalseChecks() {
        //Arrange
        String userName = "Test";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");

        when(groupService.checkService.doesUserExist(userName, groupService.getUsers())).thenReturn(validationResult1);

        //act
        ValidationResult validationResult = groupService.createUser(userName);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testCreateGroupPositiveChecks() {
        //Arrange
        String groupDescription = "description";
        String groupName = "name";
        String groupCourse = "course";
        String groupCreator = "creator";
        String groupType = "type";

        // Es wird kein ValidationResult erzeugt, da es auch keine checks gibt.

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performGroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);

        //act
        ValidationResult validationResult = groupService1.createGroup(groupDescription, groupName, groupCourse, groupCreator, groupType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testDeleteGroupPositiveChecks() {
        //Arrange
        String groupId = "1";
        String userName = "Test";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();

        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performGroupDeletionEvent(userName, groupId);

        //act
        ValidationResult validationResult = groupService1.deleteGroup(groupId, userName);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testDeleteGroupFalseChecks() {
        //Arrange
        String groupId = "1";
        String userName = "Test";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        validationResult1.addError("Test");

        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performGroupDeletionEvent(userName, groupId);

        //act
        ValidationResult validationResult = groupService1.deleteGroup(groupId, userName);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testUpdateGroupPropertiesPositiveChecks() {
        //Arrange
        String groupId = "1";
        String updatedBy = "updatedBy";
        String groupName = "Test";
        String description = "description";
        String groupType = "groupType";

        ValidationResult validationResult1 = new ValidationResult();

        when(groupService.checkService.isAdmin(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performGroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);

        //act
        ValidationResult validationResult = groupService1.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testUpdateGroupPropertiesFalseChecks() {
        //Arrange
        String groupId = "1";
        String updatedBy = "updatedBy";
        String groupName = "Test";
        String description = "description";
        String groupType = "groupType";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");

        when(groupService.checkService.isAdmin(updatedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);

        GroupService groupService1 = Mockito.spy(groupService);
        Mockito.doNothing().when(groupService1).performGroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);

        //act
        ValidationResult validationResult = groupService1.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testCollectCheck() {
        //Arrange
        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();
        validationResult1.addError("Das ist.");
        validationResult1.addError("ein.");
        validationResult3.addError("Test");

        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(validationResult1);
        validationResults.add(validationResult2);
        validationResults.add(validationResult3);

        //act
        ValidationResult validationResult = groupService.collectCheck(validationResults);
        System.out.println(validationResult.getErrorMessages().get(0));
        System.out.println(validationResult3.getErrorMessages().get(0));

        //assert
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrorMessages().get(0)).isEqualTo("Das ist. ein.");
        assertThat(validationResult.getErrorMessages().get(1)).isEqualTo("Test");
    }

    @Tag("GroupTest")
    @Test
    void testGetActiveMembersOfGroupWithOneActiveMember() {
        // Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();

        // Act
        List<Membership> activeMembersOfGroup = groupService.getActiveMembersOfGroup(groupId);

        //Assert
        assertThat(activeMembersOfGroup).hasSize(1);
        assertThat(activeMembersOfGroup.get(0).getUser()).isEqualTo(testSetup.users.get("Diego"));
    }

    @Tag("GroupTest")
    @Test
    void testGetActiveMembersOfGroupWithTwoActiveMembers() {
        // Arrange
        String groupId = testSetup.groupOne.getGroupId().toString();

        // Act
        List<Membership> activeMembersOfGroup = groupService.getActiveMembersOfGroup(groupId);

        //Assert
        assertThat(activeMembersOfGroup).hasSize(2);
        assertThat(activeMembersOfGroup.get(0).getUser()).isEqualTo(testSetup.users.get("Max"));
        assertThat(activeMembersOfGroup.get(1).getUser()).isEqualTo(testSetup.users.get("Stela"));

    }

    @Tag("GroupTest")
    @Test
    void testgetGroupsWhereUserIsActiveWithOneMembership() {
        // Arrange
        String userName = "Diego";

        // Act
        List<Group> groupsWhereUserIsActive = groupService.getGroupsWhereUserIsActive(userName);

        // Assert
        assertThat(groupsWhereUserIsActive).hasSize(1);
    }

    @Tag("GroupTest")
    @Test
    void testgetGroupsWhereUserIsActiveWithZeroMemberships() {
        // Arrange
        String userName = "Milten";

        // Act
        List<Group> groupsWhereUserIsActive = groupService.getGroupsWhereUserIsActive(userName);

        // Assert
        assertThat(groupsWhereUserIsActive).hasSize(0);
    }

    @Tag("GroupTest")
    @Test
    void testgetPendingMembershipsWithOnePendingMembership() {
        // Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();
        Membership milten = testSetup.memberships.get(5);

        // Act
        List<Membership> pendingMemberships = groupService.getPendingMemberships(groupId);

        // Assert
        assertThat(pendingMemberships.get(0)).isEqualTo(milten);
    }

    @Tag("GroupTest")
    @Test
    void testgetPendingMembershipsWithZeroPendingMemberships() {
        // Arrange
        String groupId = testSetup.groupOne.getGroupId().toString();

        // Act
        List<Membership> pendingMemberships = groupService.getPendingMemberships(groupId);

        // Assert
        assertThat(pendingMemberships).isEmpty();
    }

    @Tag("GroupTest")
    @Test
    void testCountPendingRequestOfGroupWithOnePendingMembership() {
        // Arrange
        String groupId = testSetup.groupThree.getGroupId().toString();

        // Act
        Long countPendingRequestOfGroup = groupService.countPendingRequestOfGroup(groupId);

        // Assert
        assertThat(countPendingRequestOfGroup).isEqualTo(1);
    }

    @Tag("GroupTest")
    @Test
    void testCountPendingRequestOfGroupWithZeroPendingMembership() {
        // Arrange
        String groupId = testSetup.groupOne.getGroupId().toString();

        // Act
        Long countPendingRequestOfGroup = groupService.countPendingRequestOfGroup(groupId);

        // Assert
        assertThat(countPendingRequestOfGroup).isEqualTo(0);
    }

    @Tag("GroupTest")
    @Test
    void testGetActiveMembershipsOfUserWithOneMembership() {
        // Arrange
        String userName = "Stela";

        // Act
        List<Membership> activeMembershipsOfUser = groupService.getActiveMembershipsOfUser(userName);

        // Assert
        assertThat(activeMembershipsOfUser).hasSize(1);
        assertThat(activeMembershipsOfUser.get(0)).isEqualTo(testSetup.memberships.get(1));
    }

    @Tag("GroupTest")
    @Test
    void testGetActiveMembershipsOfUserWithZeroMembership() {
        // Arrange
        String userName = "Milten";

        // Act
        List<Membership> activeMembershipsOfUser = groupService.getActiveMembershipsOfUser(userName);

        // Assert
        assertThat(activeMembershipsOfUser).hasSize(0);
        assertThat(activeMembershipsOfUser).isEmpty();
    }

    @Tag("GroupTest")
    @Test
    void testSearchGroupsByNamePositiveCheck() {
        // Arrange
        String groupName = testSetup.groupOne.getName().toString();

        // Act
        List<Group> groups = groupService.searchGroupsByName(groupName);

        // Assert
        assertThat(groups).hasSize(1);
        assertThat(groups.get(0)).isEqualTo(testSetup.groupOne);
    }

    @Tag("GroupTest")
    @Test
    void testSearchGroupsByNameFalseCheckDeactivatedGroup() {
        // Arrange
        String groupName = testSetup.groupTwo.getName().toString();

        // Act
        List<Group> groups = groupService.searchGroupsByName(groupName);

        // Assert
        assertThat(groups).hasSize(0);
    }

    @Tag("GroupTest")
    @Test
    void testSearchGroupsByNameFalseCheck() {
        // Arrange
        String groupName = "catSmashedKe32577qwr11412141271247ontokeyboard";

        // Act
        List<Group> groups = groupService.searchGroupsByName(groupName);

        // Assert
        assertThat(groups).isEmpty();
    }

    @Tag("GroupTest")
    @Test
    void testSearchUserByNamePositiveCheckOneUser() {
        // Arrange
        String userName = "Diego";

        // Act
        List<String> names = groupService.searchUserByName(userName);

        // Assert
        assertThat(names).hasSize(1);
        assertThat(names.get(0)).isEqualTo(userName);
    }

    @Tag("GroupTest")
    @Test
    void testSearchUserByNamePositiveCheckTwoUsers() {
        // Arrange
        String userName = "ST";

        // Act
        List<String> names = groupService.searchUserByName(userName);

        // Assert
        assertThat(names).hasSize(2);
        assertThat(names).contains("Stela");
        assertThat(names).contains("Steve");
    }

    @Tag("GroupTest")
    @Test
    void testSearchUserByNameFalseCheck() {
        // Arrange
        String userName = "Xardas";

        // Act
        List<String> names = groupService.searchUserByName(userName);

        // Assert
        assertThat(names).hasSize(0);
    }
}