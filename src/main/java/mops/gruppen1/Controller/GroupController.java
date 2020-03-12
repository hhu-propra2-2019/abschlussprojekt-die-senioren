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
    public String groupCreation()   {
        return "erstellen";
    }

    @GetMapping("/")
    @Secured("ROLE_studentin")
    public String index(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "index";
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
