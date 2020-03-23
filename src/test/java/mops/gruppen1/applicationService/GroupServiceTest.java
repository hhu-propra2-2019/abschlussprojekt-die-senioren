package mops.gruppen1.applicationService;

import org.graalvm.compiler.nodes.extended.ArrayRangeWrite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GroupServiceTest {

    GroupService groupService;

    @BeforeEach
    public void setUp() {
        EventService eventServiceMock = mock(EventService.class);
        CheckService checkServiceMock = mock(CheckService.class);
        this.groupService = new GroupService(eventServiceMock,checkServiceMock);
    }


    @Test
    void testAssignMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isPublic()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.isNotMember()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.assignMembership(userName,groupId,membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

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

        when(checkServiceMock.isPublic()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.isNotMember()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.assignMembership(userName,groupId,membershipType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Test
    void testAcceptMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsPending()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.acceptMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Test
    void testAcceptMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsPending()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.acceptMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Test
    void testRejectMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsPending()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.rejectMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

    @Test
    void testRejectMembershipFalseChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";

        ValidationResult validationResult1 = new ValidationResult();
        validationResult1.addError("Test");
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsPending()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.rejectMembership(userName,groupId);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

    @Test
    void testRequestMembershipPositiveChecks() {
        //Arrange
        String userName = "Test";
        String groupId = "1";
        String membershipType = "VIEWER";

        ValidationResult validationResult1 = new ValidationResult();
        ValidationResult validationResult2 = new ValidationResult();
        ValidationResult validationResult3 = new ValidationResult();

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.isNotMember()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.requestMembership(userName,groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isTrue();
    }

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

        when(checkServiceMock.isRestricted()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult2);
        when(checkServiceMock.isNotMember()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.requestMembership(userName,groupId, membershipType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsAcitve()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

        //act
        ValidationResult validationResult = groupService.deleteMembership(userName, groupId, deletedBy);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult2);
        when(checkServiceMock.isAdmin()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.isMember()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult3);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);
        when(checkServiceMock.isMember()).thenReturn(validationResult2);
        when(checkServiceMock.membershipIsActive()).thenReturn(validationResult3);

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

        when(checkServiceMock.doesUserExist()).thenReturn(validationResult1);

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

        when(checkServiceMock.doesUserExist()).thenReturn(validationResult1);

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

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);

        //act
        ValidationResult validationResult = groupService.deleteGroup(groupId, userName);

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
        validationResult1.addError("Test");

        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);

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

        when(checkServiceMock.isAdmin()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);

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

        when(checkServiceMock.isAdmin()).thenReturn(validationResult1);
        when(checkServiceMock.isGroupActive()).thenReturn(validationResult1);

        //act
        ValidationResult validationResult = groupService.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);

        //assert
        assertThat(validationResult.isValid()).isFalse();
    }


}