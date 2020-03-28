package mops.gruppen1.Controller;

import mops.gruppen1.applicationService.RestService;
import mops.gruppen1.data.DAOs.GroupDAO;
import mops.gruppen1.data.DAOs.UpdatedGroupsDAO;
import mops.gruppen1.data.DAOs.UserDAO;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class InteractionControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    RestService restServiceMock;

    @BeforeEach
    private void setSecurityContext() {
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_api_user");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_missingUserName")
    @Test
    void testIsUserInGroupMissingUsername() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("groupId", "testGruppe")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_missingGroupId")
    @Test
    void testIsUserInGroupMissingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("userName", "testUser")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_AllParamsMissing")
    @Test
    void testIsUserInGroupAllParamsMissing() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_True")
    @Test
    void testIsUserInGroupTrue() throws Exception {

        //Arrange
        when(restServiceMock.isUserInGroup(any(), any())).thenReturn(true);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("userName", "testUser")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isUserInGroup").value(true));
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_False")
    @Test
    void testIsUserInGroupFalse() throws Exception {

        //Arrange
        when(restServiceMock.isUserInGroup(any(), any())).thenReturn(false);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("userName", "testUser")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isUserInGroup").value(false));
    }


    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_missingUserName")
    @Test
    void testisUserAdminInGroupMissingUsername() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("groupId", "testGruppe")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_missingGroupId")
    @Test
    void testisUserAdminInGroupMissingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("userName", "testUser")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_AllParamsMissing")
    @Test
    void testisUserAdminInGroupAllParamsMissing() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_True")
    @Test
    void testisUserAdminInGroupTrue() throws Exception {

        //Arrange
        when(restServiceMock.isUserAdminInGroup(any(), any())).thenReturn(true);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("userName", "testUser")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isUserAdminInGroup").value(true));
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_False")
    @Test
    void testisUserAdminInGroupFalse() throws Exception {

        //Arrange
        when(restServiceMock.isUserAdminInGroup(any(), any())).thenReturn(false);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("userName", "testUser")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isUserAdminInGroup").value(false));
    }

    @Tag("InteractionController")
    @DisplayName("doesGroupExist_missingGroupId")
    @Test
    void testdoesGroupExistMissingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("doesGroupExist_False")
    @Test
    void testdoesGroupExistFalse() throws Exception {

        //Arrange
        when(restServiceMock.doesActiveGroupExist(any())).thenReturn(false);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doesGroupExist").value(false));
    }

    @Tag("InteractionController")
    @DisplayName("doesGroupExist_True")
    @Test
    void testdoesGroupExistTrue() throws Exception {

        //Arrange
        when(restServiceMock.doesActiveGroupExist(any())).thenReturn(true);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doesGroupExist").value(true));
    }

    @Tag("InteractionController")
    @DisplayName("returnAllGroups_incompatibleParameter")
    @Test
    void testreturnAllGroupsIncompatibleParameter() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnAllGroups")
                .param("lastEventId", "x"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("returnAllGroups_noUpdatedGroups")
    @Test
    void testreturnAllGroupsNoUpdatedGroups() throws Exception {

        //Arrange
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(2);
        when(restServiceMock.getUpdatedGroups(any())).thenReturn(updatedGroupsDAO);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnAllGroups")
                .param("lastEventId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(2))
                .andExpect(jsonPath("$.groupDAOs").isArray())
                .andExpect(jsonPath("$.groupDAOs").isEmpty());
    }

    @Tag("InteractionController")
    @DisplayName("returnAllGroups_withOneUpdatedGroup")
    @Test
    void testreturnAllGroupsWithOneUpdatedGroup() throws Exception {

        //Arrange
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(3);
        GroupDAO groupDAO = new GroupDAO("testGroup", "groupName", "This is a description.", "ACTIVE");
        updatedGroupsDAO.addGroupDAO(groupDAO);

        when(restServiceMock.getUpdatedGroups(any())).thenReturn(updatedGroupsDAO);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnAllGroups")
                .param("lastEventId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.groupDAOs[0].groupId").value("testGroup"))
                .andExpect(jsonPath("$.groupDAOs[0].groupName").value("groupName"))
                .andExpect(jsonPath("$.groupDAOs[0].groupDescription").value("This is a description."))
                .andExpect(jsonPath("$.groupDAOs[0].status").value("ACTIVE"));
    }

    @Tag("InteractionController")
    @DisplayName("returnAllGroups_withTwoUpdatedGroups")
    @Test
    void testreturnAllGroupsWithTwoUpdatedGroups() throws Exception {

        //Arrange
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(3);
        GroupDAO groupDAO1 = new GroupDAO("testGroup", "groupName", "This is a description.", "ACTIVE");
        GroupDAO groupDAO2 = new GroupDAO("testGroup", "groupName", "Info1", "This is a description.", "ACTIVE");
        updatedGroupsDAO.addGroupDAO(groupDAO1);
        updatedGroupsDAO.addGroupDAO(groupDAO2);

        when(restServiceMock.getUpdatedGroups(any())).thenReturn(updatedGroupsDAO);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnAllGroups")
                .param("lastEventId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.groupDAOs[0].groupId").value("testGroup"))
                .andExpect(jsonPath("$.groupDAOs[0].groupName").value("groupName"))
                .andExpect(jsonPath("$.groupDAOs[0].groupDescription").value("This is a description."))
                .andExpect(jsonPath("$.groupDAOs[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.groupDAOs[1].groupId").value("testGroup"))
                .andExpect(jsonPath("$.groupDAOs[1].groupName").value("groupName"))
                .andExpect(jsonPath("$.groupDAOs[1].course").value("Info1"))
                .andExpect(jsonPath("$.groupDAOs[1].groupDescription").value("This is a description."))
                .andExpect(jsonPath("$.groupDAOs[1].status").value("ACTIVE"));
    }

    @Tag("InteractionController")
    @DisplayName("returnUsersOfGroup_missingGroupid")
    @Test
    void testreturnUsersOfGroupMissingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnUsersOfGroup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("returnUsersOfGroup_noUsers")
    @Test
    void testreturnUsersOfGroupNoUsers() throws Exception {

        //Arrange
        List<UserDAO> userDAOs = new ArrayList<>();
        when(restServiceMock.getUsersOfGroup(any())).thenReturn(userDAOs);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnUsersOfGroup")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Tag("InteractionController")
    @DisplayName("returnUsersOfGroup_twoUsers")
    @Test
    void testreturnUsersOfGroupTwoUsers() throws Exception {

        //Arrange
        List<UserDAO> userDAOs = new ArrayList<>();
        UserDAO userDAO1 = new UserDAO("user1");
        UserDAO userDAO2 = new UserDAO("user2");
        userDAOs.add(userDAO1);
        userDAOs.add(userDAO2);
        when(restServiceMock.getUsersOfGroup(any())).thenReturn(userDAOs);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnUsersOfGroup")
                .param("groupId", "testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("user1"))
                .andExpect(jsonPath("$[1].userName").value("user2"));
    }

    @Tag("InteractionController")
    @DisplayName("returnGroupsOfUsers_missingUserName")
    @Test
    void testreturnGroupsOfUsersMissingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnGroupsOfUsers"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("returnGroupsOfUsers_noGroups")
    @Test
    void testreturnGroupsOfUsersNoGroups() throws Exception {

        //Arrange
        List<GroupDAO> groupDAOs = new ArrayList<>();
        when(restServiceMock.getGroupsOfUser(any())).thenReturn(groupDAOs);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnGroupsOfUsers")
                .param("userName", "user1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Tag("InteractionController")
    @DisplayName("returnGroupsOfUsers_twoUsers")
    @Test
    void testreturnGroupsOfUsersTwoUsers() throws Exception {

        //Arrange
        List<GroupDAO> groupDAOs = new ArrayList<>();
        GroupDAO groupDAO1 = new GroupDAO("testGroup", "groupName", "This is a description.", "ACTIVE");
        GroupDAO groupDAO2 = new GroupDAO("testGroup", "groupName", "Info1","This is a description.", "ACTIVE");
        groupDAOs.add(groupDAO1);
        groupDAOs.add(groupDAO2);
        when(restServiceMock.getGroupsOfUser(any())).thenReturn(groupDAOs);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/returnGroupsOfUsers")
                .param("userName", "user1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].groupId").value("testGroup"))
                .andExpect(jsonPath("$[0].groupName").value("groupName"))
                .andExpect(jsonPath("$[0].groupDescription").value("This is a description."))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].groupId").value("testGroup"))
                .andExpect(jsonPath("$[1].groupName").value("groupName"))
                .andExpect(jsonPath("$[1].course").value("Info1"))
                .andExpect(jsonPath("$[1].groupDescription").value("This is a description."))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"));
    }

}
