package mops.gruppen1.applicationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    GroupService groupService;

    @BeforeEach
    public void setUp() {
        EventService eventServiceMock = mock(EventService.class);
        CheckService checkServiceMock = mock(CheckService.class);
        this.groupService = new GroupService(eventServiceMock,checkServiceMock);
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipPositiveChecks() {
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

        //act
        ValidationResult validationResult = groupService.assignMembership(userName,groupId,membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testAssignMembershipFalseChecks() {
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

        //act
        ValidationResult validationResult = groupService.assignMembership(userName,groupId,membershipType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testAcceptMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        
        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.acceptMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testAcceptMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.acceptMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Tag("GroupTest")
    @Test
    void testRejectMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.rejectMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Tag("GroupTest")
    @Test
    void testRejectMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(groupService.checkService.isRestricted(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipPending(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.rejectMembership(userName,groupId);

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

        //act
        ValidationResult validationResult = groupService.requestMembership(userName,groupId, membershipType);

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

        //act
        ValidationResult validationResult = groupService.requestMembership(userName,groupId, membershipType);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.updateMembership(userName, groupId,  updatedBy, updatedTo);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.updateMembership(userName, groupId,  updatedBy, updatedTo);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.deleteMembership(userName, groupId, deletedBy);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isAdmin(deletedBy, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.deleteMembership(userName, groupId, deletedBy);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.resignMembership(userName, groupId);

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

        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult1);
        when(groupService.checkService.isMember(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult2);
        when(groupService.checkService.isMembershipActive(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.resignMembership(userName, groupId);

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

        //act
        ValidationResult validationResult = groupService.createGroup(groupDescription, groupName, groupCourse, groupCreator, groupType);

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

        System.out.println(validationResult1.isValid());
        System.out.println(validationResult2.isValid());

        when(groupService.checkService.isAdmin(userName, groupId, groupService.getGroups(), groupService.getUsers(),
                groupService.getUserToMembers())).thenReturn(validationResult1);
        when(groupService.checkService.isGroupActive(groupId, groupService.getGroups())).thenReturn(validationResult2);

        //act
        ValidationResult validationResult = groupService.deleteGroup(groupId, userName);

        //assert
        System.out.println(validationResult.isValid());
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

        //act
        ValidationResult validationResult = groupService.deleteGroup(groupId, userName);

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

        //act
        ValidationResult validationResult = groupService.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);

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

        //act
        ValidationResult validationResult = groupService.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }


}