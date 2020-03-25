package mops.gruppen1.Controller;

import lombok.AllArgsConstructor;
import mops.gruppen1.applicationService.ApplicationService;
import mops.gruppen1.applicationService.GroupService;
import mops.gruppen1.domain.Module;
import mops.gruppen1.security.Account;
import mops.gruppen1.domain.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/gruppen1")
public class GroupController {

    @Autowired
    private ApplicationService applicationService;
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
    public String groupCreation(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
            //TODO Brauchen hier Methoden zum fetchen aller User und Module
            //TODO Informieren, wie CSV Einbindung funktioniert
            //Get all users from user - Hashmap
            //model.addAttribute("allUsers",groupService.getUsers().values());
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "erstellen";
    }

    @PostMapping("/erstellen")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupCreation (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search,
                                 @RequestParam(value = "groupname") String groupName,
                                 @RequestParam(value = "groupModule") String module,
                                 @RequestParam(value = "groupType") String groupType,
                                 @RequestParam(value = "groupDescription", required = false) String groupDescription,
                                 @RequestParam(value = "csv", required = false) String csvFileName,
                                 @RequestParam(value = "members", required = false) List<String> members)
    {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            //account - Name gleich Username
            applicationService.createGroup(groupDescription,groupName,module,account.getName(),groupType,members);
         }

        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "redirect:/";
    }

    @GetMapping("/description")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String descriptionChange (KeycloakAuthenticationToken token, Model model,
                                     @RequestParam(name = "search") Optional search,
                                     @RequestParam(name = "groupId") String groupId) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            //model.addAttribute("memberships",groupService.getUserToMembers().get(account.getName()));
            //model.addAttribute("placeholder_groupname",groupService.getGroups().get(groupId).getName());
            //model.addAttribute("placeholder_groupdescription",groupService.getGroups().get(groupId).getDescription());
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "changeDescription"+groupId;
    }

    @PostMapping("/description/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String descriptionChange (KeycloakAuthenticationToken token, Model model,
                                     @RequestParam(name = "search") Optional search,
                                     @RequestParam(name = "groupName") String groupname,
                                     @RequestParam(name = "groupDescription") String groupDescription) {

        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);

        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "changeDescription";
    }

    @GetMapping("/memberships")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String membershipChange(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "changeMemberships";
    }

    @GetMapping("/viewer") // /{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String viewerView (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
                            // , @PathVariable("id") String id) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "gruppenViewer";
    }

    @GetMapping("/admin") ///{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String adminView (KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
            // , @PathVariable("id") String id) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
           // model.addAttribute("groupId",id);
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "gruppenAdmin";
    }


    @GetMapping("/")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String index(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            //String userName = "userX";
            //applicationService.groupService.init();
            //List<Membership> memberships = applicationService.getMembershipsOfUser(userName);
            //System.out.println(memberships);
            //model.addAttribute("memberships",memberships);
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "index";
    }


    @GetMapping("/groupRequests")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupRequests(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        if (search.isPresent()) {
            return searchGroups(search);
        }
        return "groupRequests";
    }


    @GetMapping("/searchResults")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String groupSearch(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
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
    public String groupDescription(KeycloakAuthenticationToken token, Model model, @RequestParam(name = "search") Optional search) {
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
