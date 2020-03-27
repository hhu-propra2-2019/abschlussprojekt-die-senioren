package mops.gruppen1.applicationService;

import mops.gruppen1.domain.events.TestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.configuration.ImockitoConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    TestSetup testSetup;
    ApplicationService applicationService;

    @BeforeEach
    void testSetup() {
        this.testSetup = new TestSetup();
        EventService eventService = mock(EventService.class);
        CheckService checkService = mock(CheckService.class);
        GroupService groupService = new GroupService(eventService, checkService,
                testSetup.groupToMembers, testSetup.userToMembers, testSetup.groups,
                testSetup.users, testSetup.groupThree.getGroupId().toString());
        this.applicationService = new ApplicationService(groupService);
    }

    @Tag("ApplicationServiceTest")
    @Test
    void createGroup() {
        // Arrange
        String groupDescription = "Hafenstadt ProPra2 Lerngruppe";
        String groupName = "Khorinis";
        String groupCourse = "Schwertkämpfen";
        String groupCreator = "Diego";
        String groupType = "PUBLIC";
        List<String> users = new ArrayList<>();
        String stela = "Stela";
        String vartras = "Vatras";
        users.add(stela);
        users.add(vartras);

        when(applicationService.groupService.checkService.doesUserExist(any(),any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isPublic(any(),any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isGroupActive(any(), any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isNotMember(any(), any(), any(), any(), any()))
                .thenReturn(new ValidationResult());

        // Act
        ValidationResult validationResult = applicationService.createGroup(groupDescription, groupName, groupCourse, groupCreator, groupType, users);

        // Assert
        assertThat(testSetup.groups).hasSize(4);
        assertThat(testSetup.users).hasSize(7);
    }


    @Tag("ApplicationServiceTest")
    @Test
    void joinGroupMembershipAssignment() {
        // Arrange
        String userName = "Neo";
        String groupId = testSetup.groupOne.getGroupId().toString();
        String requestMessage = "Bist du hier der Anführer?";

        when(applicationService.groupService.checkService.isPublic(any(),any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isGroupActive(any(), any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isNotMember(any(), any(), any(), any(), any()))
                .thenReturn(new ValidationResult());

        // Act
        ValidationResult validationResult = applicationService.joinGroup(userName, groupId, requestMessage);

        // Assert
        verify(applicationService.groupService.checkService, times(1)).isPublic(any(), any());
        verify(applicationService.groupService.checkService, times(1)).isGroupActive(any(), any());
        verify(applicationService.groupService.checkService, times(1)).isNotMember(any(), any(), any(), any(), any());
        verify(applicationService.groupService.checkService, times(0)).isRestricted(any(), any());
    }

    @Tag("ApplicationServiceTest")
    @Test
    void joinGroupMembershipRequest() {
        // Arrange
        String userName = "Neo";
        String groupId = testSetup.groupThree.getGroupId().toString();
        String requestMessage = "Bist du hier der Anführer?";

        when(applicationService.groupService.checkService.isRestricted(any(),any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isGroupActive(any(), any()))
                .thenReturn(new ValidationResult());
        when(applicationService.groupService.checkService.isNotMember(any(), any(), any(), any(), any()))
                .thenReturn(new ValidationResult());

        // Act
        ValidationResult validationResult = applicationService.joinGroup(userName, groupId, requestMessage);

        // Assert
        verify(applicationService.groupService.checkService, times(1)).isRestricted(any(), any());
        verify(applicationService.groupService.checkService, times(1)).isGroupActive(any(), any());
        verify(applicationService.groupService.checkService, times(1)).isNotMember(any(), any(), any(), any(), any());
        verify(applicationService.groupService.checkService, times(0)).isPublic(any(), any());
    }
  
      @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_FilenameTooShort")
    @Test
    public void testUploadCsvFilenameTooShort(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> usernames = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn(".csv");

        //Act & Assert
        Throwable thrown = assertThrows(Exception.class, ()
                -> applicationService.uploadCsv(multipartFile,usernames));

        assertThat(thrown.getMessage()).isEqualTo("Kein gültiger Dateiname");
    }

    @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_WrongFileFormat")
    @Test
    public void testUploadCsvWrongFileFormat(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> usernames = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");

        //Act & Assert
        Throwable thrown = assertThrows(Exception.class, ()
                -> applicationService.uploadCsv(multipartFile,usernames));

        assertThat(thrown.getMessage()).isEqualTo("Diese Datei ist keine Csv-Datei");
    }

    @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_ValidInputFile")
    @Test
    public void testUploadCsvValidInputFile(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> csvUsernames = new ArrayList<>();
        csvUsernames.add("user1");
        csvUsernames.add("user2");
        List<String> usernames = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        ApplicationService applicationService1 = Mockito.spy(applicationService);

        try {
            when(applicationService1.extractUsernamesFromCsv(any())).thenReturn(csvUsernames);
        }
        catch(Exception e) {}

        //Act
        try {
            resultList =  applicationService.uploadCsv(multipartFile, usernames);
        }
        catch(Exception e) {}

        //Assert
        assertThat(resultList.contains("user1"));
        assertThat(resultList.contains("user2"));
        assertThat(resultList.size() == 2);
    }
}

