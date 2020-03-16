package mops.gruppen1.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test for Controller
 * - Might have to be updated after Keycloak update
 */
@AutoConfigureMockMvc
@SpringBootTest
class GroupControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Index - Seite.")
    @Test
    void testIndex() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Admin - View einer Gruppe.")
    @Test
    void testAdminView() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("gruppenAdmin"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Viewer - View einer Gruppe.")
    @Test
    void testMemberView() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/viewer"))
                .andExpect(status().isOk())
                .andExpect(view().name("gruppenViewer"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Gruppen-Erstellungs  Seite.")
    @Test
    void testGroupCreation() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/erstellen"))
                .andExpect(status().isOk())
                .andExpect(view().name("erstellen"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Viewer - View einer Gruppe.")
    @Test
    void testChangeGroupDescription() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/description"))
                .andExpect(status().isOk())
                .andExpect(view().name("changeDescription"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Member-Edit Seite einer Gruppe.")
    @Test
    void testMembershipChange() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/memberships"))
                .andExpect(status().isOk())
                .andExpect(view().name("changeMemberships"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Anfragenseite neuer Gruppenmitglieder")
    @Test
    void testGroupRequests() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/groupRequests"))
                .andExpect(status().isOk())
                .andExpect(view().name("groupRequests"));
    }

    @Tag("controller")
    @DisplayName("Teste Verbindung zur Error-Seite")
    @Test
    void testErrorPage() throws Exception {
        Set<String> roles = new HashSet<String>();
        roles.add("studentin");
        roles.add("orga");
        KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(
                        Mockito.mock(KeycloakPrincipal.class, Mockito.RETURNS_DEEP_STUBS),
                        roles,
                        Mockito.mock(RefreshableKeycloakSecurityContext.class)),
                true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        mvc.perform(get("/gruppen1/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}