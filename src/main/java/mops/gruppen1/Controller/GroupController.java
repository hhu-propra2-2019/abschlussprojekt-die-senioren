package mops.gruppen1.Controller;

import lombok.AllArgsConstructor;
import mops.gruppen1.applicationService.GroupService;
import mops.gruppen1.security.Account;
import mops.gruppen1.domain.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/gruppen1")
public class GroupController {

    private GroupService groupService;
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

    private String searchGroups(Optional search) {
        return "searchResults";
    }


    @GetMapping("/erstellen")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupCreation (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
           return searchGroups(search);
        }
        return "erstellen";
    }

    @GetMapping("/description")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String descriptionChange (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "changeDescription";
    }

    @GetMapping("/memberships")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String membershipChange (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
           return searchGroups(search);
        }
        return "changeMemberships";
    }

    @GetMapping("/viewer/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String viewerView (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search,
                              @PathVariable("id") long id) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
           return searchGroups(search);
        }
        return "gruppenViewer";
    }

    @GetMapping("/admin/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String adminView (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search,
                             @PathVariable("id") long id) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
           return searchGroups(search);
        }
        return "gruppenAdmin";
    }

    @GetMapping("/")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String index (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            User user = new User(new Username(account.getName()));
            model.addAttribute("user",user);
            model.addAttribute("memberships",groupService.getUserToMembers().get(user.getUsername().getUsername()));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "index";
    }



    @GetMapping("/groupRequests")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupRequests (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "groupRequests";
    }


    @GetMapping("/error")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String errorPage (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "error";
    }

    @GetMapping("/searchResults")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupSearch (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "searchResults";
    }

    @GetMapping("/requestMessage")
    @Secured("ROLE_studentin")
    public String groupDescription (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "requestDescription";
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
