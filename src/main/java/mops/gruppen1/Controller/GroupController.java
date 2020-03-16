package mops.gruppen1.Controller;

import mops.gruppen1.security.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/gruppen1")
public class GroupController {

    /**
     * Nimmt das Authentifizierungstoken von Keycloak und erzeugt ein AccountDTO für die Views.
     * (Entnommen aus der Vorlage 'KeycloakDemo'
     *
     * @param token
     * @return neuen Account der im Template verwendet wird
     */
    private Account createAccountFromPrincipal(KeycloakAuthenticationToken token) {
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    @GetMapping("/erstellen")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupCreation (KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "erstellen";
    }

    @GetMapping("/description")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String descriptionChange(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "changeDescription";
    }

    @GetMapping("/memberships")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String membershipChange(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "changeMemberships";
    }

    @GetMapping("/viewer")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String viewerView(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "gruppenViewer";
    }

    @GetMapping("/admin")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String adminView(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "gruppenAdmin";
    }

    @GetMapping("/")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String index(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "index";
    }

    @GetMapping("/groupRequests")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupRequests(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "groupRequests";
    }

    @GetMapping("/error")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String errorPage(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "error";
    }

    /**
     * Method can be used once the log-out button works properly
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/gruppen1/";
    }
}
