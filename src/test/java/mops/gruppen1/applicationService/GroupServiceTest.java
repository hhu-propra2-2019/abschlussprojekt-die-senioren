package mops.gruppen1.applicationService;

import org.graalvm.compiler.nodes.extended.ArrayRangeWrite;
import org.junit.jupiter.api.BeforeEach;
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

}