package mops.gruppen1.Controller;

import mops.gruppen1.applicationService.RestService;
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

import java.util.HashSet;
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
    void testIsUserInGroup_missingUsername() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("groupId", "testGruppe")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_missingGroupId")
    @Test
    void testIsUserInGroup_missingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup")
                .param("userName", "testUser")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_AllParamsMissing")
    @Test
    void testIsUserInGroup_AllParamsMissing() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserInGroup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("IsUserInGroup_True")
    @Test
    void testIsUserInGroup_True() throws Exception {

        //Arrange
        when(restServiceMock.isUserInGroup(any(),any())).thenReturn(true);

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
    void testIsUserInGroup_False() throws Exception {

        //Arrange
        when(restServiceMock.isUserInGroup(any(),any())).thenReturn(false);

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
    void testisUserAdminInGroup_missingUsername() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("groupId", "testGruppe")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_missingGroupId")
    @Test
    void testisUserAdminInGroup_missingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup")
                .param("userName", "testUser")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_AllParamsMissing")
    @Test
    void testisUserAdminInGroup_AllParamsMissing() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/isUserAdminInGroup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("isUserAdminInGroup_True")
    @Test
    void testisUserAdminInGroup_True() throws Exception {

        //Arrange
        when(restServiceMock.isUserAdminInGroup(any(),any())).thenReturn(true);

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
    void testisUserAdminInGroup_False() throws Exception {

        //Arrange
        when(restServiceMock.isUserAdminInGroup(any(),any())).thenReturn(false);

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
    void testdoesGroupExist_missingGroupId() throws Exception {

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Tag("InteractionController")
    @DisplayName("doesGroupExist_False")
    @Test
    void testdoesGroupExist_False() throws Exception {

        //Arrange
        when(restServiceMock.doesActiveGroupExist(any())).thenReturn(false);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist")
                .param("groupId","testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doesGroupExist").value(false));
    }

    @Tag("InteractionController")
    @DisplayName("doesGroupExist_True")
    @Test
    void testdoesGroupExist_True() throws Exception {

        //Arrange
        when(restServiceMock.doesActiveGroupExist(any())).thenReturn(true);

        //Act & Assert
        this.mvc.perform(get("/gruppen1/doesGroupExist")
                .param("groupId","testGroup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doesGroupExist").value(true));
    }
}
