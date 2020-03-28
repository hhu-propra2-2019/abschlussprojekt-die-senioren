package mops.gruppen1.controller;

import lombok.AllArgsConstructor;
import mops.gruppen1.applicationService.ApplicationService;
import mops.gruppen1.applicationService.ValidationResult;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.security.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        applicationService.createUser(token.getName());
        return new Account(
                principal.getName(),
                principal.getKeycloakSecurityContext().getIdToken().getEmail(),
                null,
                token.getAccount().getRoles());
    }

    private String searchGroups(Optional search, Model model) {
        String result = search.get().toString();
        List <Group> searchResult = applicationService.searchGroupByName(result);
        model.addAttribute("matchedGroups",searchResult);
        return "searchResults";
    }


    @GetMapping("/erstellen")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String createGroupOverview(KeycloakAuthenticationToken token, Model model,
                                @RequestParam(name = "search") Optional search) {
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        return "erstellen";
    }

    @PostMapping("/erstellen")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String createGroup(KeycloakAuthenticationToken token, Model model,
                                 @RequestParam(name = "search") Optional search,
                                 @RequestParam(value = "groupname") String groupName,
                                 @RequestParam(value = "groupModule") String module,
                                 @RequestParam(value = "groupType") String groupType,
                                 @RequestParam(value = "groupDescription", required = false) String groupDescription,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 @RequestParam(value = "members", required = false) List<String> members)
    {
        Account account = createAccountFromPrincipal(token);
        model.addAttribute("account", account);
        //account - Name gleich Username

        try {
            if (!file.isEmpty()){
                members = applicationService.uploadCsv(file, members);
            }
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "redirect:/gruppen1/erstellen";
        }
        applicationService.createGroup(groupDescription,groupName,module,account.getName(),groupType,members);
        return "redirect:/gruppen1/";
    }




    @GetMapping("/description/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String changeGroupPropertiesOverview(KeycloakAuthenticationToken token, Model model,
                                      @RequestParam(name = "search") Optional search,
                                      @PathVariable("id") String id){
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        ValidationResult validation = applicationService.isActiveAdmin(token.getName(), id);
        if(validation.isValid()) {
            model.addAttribute("groupId", id);
            Group group = applicationService.getGroup(id);
            model.addAttribute("placeholder_groupname", group.getName().toString());
            model.addAttribute("placeholder_groupdescription", group.getDescription().toString());
            model.addAttribute("placeholder_grouptype", group.getGroupType().toString());
            return "changeProperties";
        }
        return "redirect:/gruppen1/";
    }

    @PostMapping("/description/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String changeGroupProperties(KeycloakAuthenticationToken token, Model model,
                                     @RequestParam(value = "groupName") String groupname,
                                     @RequestParam(value = "groupDescription") String groupDescription,
                                     @RequestParam(value = "groupType") String groupType,
                                     @PathVariable("id") String groupId) {
        ValidationResult validation = applicationService.isActiveAdmin(token.getName(), groupId);
        if(validation.isValid()) {
            applicationService.updateGroupProperties(groupId, token.getName(), groupname, groupDescription, groupType);
            return "redirect:/gruppen1/admin/{id}";
        }
        return "redirect:/gruppen1/";
    }

    @GetMapping("/memberships/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String changeMembershipOverview(KeycloakAuthenticationToken token, Model model,
                                   @RequestParam(name = "search") Optional search,
                                   @PathVariable("id") String groupId) {
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        ValidationResult validation = applicationService.isActiveAdmin(token.getName(), groupId);
        if(validation.isValid()) {
            model.addAttribute("groupId", groupId);
            model.addAttribute("members", applicationService.getMembersOfGroup(groupId));
            return "changeMemberships";
        }
        return "redirect:/gruppen1/";
    }

    @PostMapping("/memberships/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String changeMembership(KeycloakAuthenticationToken token, Model model,
                                   @RequestParam(value="username") String username,
                                   @RequestParam(value="action") String action,
                                   @PathVariable("id") String groupId) {
        ValidationResult validation = applicationService.isActiveAdmin(token.getName(), groupId);
        if(validation.isValid()) {
            deleteOrUpdateMembership(token, username, action, groupId);
            return "redirect:/gruppen1/memberships/{id}";
        }
        return "redirect:/gruppen1/";
    }

    private void deleteOrUpdateMembership(KeycloakAuthenticationToken token, 
                                          @RequestParam("username") String username, 
                                          @RequestParam("action") String action, 
                                          @PathVariable("id") String groupId) {
        if (action.equals("delete")) {
            applicationService.deleteMember(username, groupId, token.getName());
        } else if (action.equals("change")) {
            applicationService.updateMembership(username, groupId, token.getName());
        }
    }

    @GetMapping("/viewer/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String showViewForViewer(KeycloakAuthenticationToken token, Model model,
                              @RequestParam(name = "search") Optional search,
                              @PathVariable("id") String id) {
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        ValidationResult validation = applicationService.isActive(token.getName(), id);
        if(validation.isValid()) {
            fillModelforDetailPages(model, id);
            return "gruppenViewer";
        }
        return "redirect:/gruppen1/";
    }

    private void fillModelforDetailPages(Model model, @PathVariable("id") String id) {
        model.addAttribute("groupId", id);
        Group group = applicationService.getGroup(id);
        model.addAttribute("members", applicationService.getActiveMembersOfGroup(id));
        model.addAttribute("groupDescription", group.getDescription().toString());
        model.addAttribute("groupName", group.getName().toString());
    }

    @PostMapping("/viewer/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String leaveGroupAsViewer(KeycloakAuthenticationToken token, Model model,
                               @PathVariable("id") String groupId) {
        ValidationResult validation = applicationService.isActive(token.getName(), groupId);
        if(validation.isValid()) {
            applicationService.resignMembership(token.getName(), groupId);
        }
        return "redirect:/gruppen1/";
    }

    @GetMapping("/admin/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String showGroupForAdmin(KeycloakAuthenticationToken token, Model model,
                             @RequestParam(name = "search") Optional search,
                             @PathVariable("id") String id) {
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        ValidationResult validationResult = applicationService.isActiveAdmin(token.getName(), id);

        if (validationResult.isValid()){
            fillModelforDetailPages(model, id);
            model.addAttribute("numberOfOpenRequests",applicationService.countPendingRequestOfGroup(id));
            return "gruppenAdmin";
        }
        return "redirect:/gruppen1/";
    }

    @PostMapping("/admin/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String performAdminActions(KeycloakAuthenticationToken token, Model model,
                               @RequestParam(value="action") String action,
                               @PathVariable("id") String groupId) {
        ValidationResult validationResult = applicationService.isActiveAdmin(token.getName(), groupId);

        if (validationResult.isValid()){
            return deleteOrResignGroup(token, action, groupId);
        }
        return "redirect:/gruppen1/";
    }

    private String deleteOrResignGroup(KeycloakAuthenticationToken token,
                                       @RequestParam("action") String action,
                                       @PathVariable("id") String groupId) {
        if (action.equals("delete")) {
            applicationService.deleteGroup(groupId, token.getName());
        } else if (action.equals("resign")) {
            ValidationResult validationResult = applicationService.resignMembership(token.getName(), groupId);
            if (isMemberResignmentNotPossible(token,validationResult, groupId)) {
                return "redirect:/gruppen1/admin/{id}";
            }
        }
        return "redirect:/gruppen1/";
    }

    private boolean isMemberResignmentNotPossible(KeycloakAuthenticationToken token,
                                                  ValidationResult validationResult,
                                                  @PathVariable("id") String groupId) {
        return !validationResult.isValid();
    }


    @GetMapping("/")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String index(KeycloakAuthenticationToken token, Model model,
                        @RequestParam(name = "search") Optional search) {
        if (token != null) {
            Account account = createAccountFromPrincipal(token);
            model.addAttribute("account", account);
            String userName = account.getName();
            List<Membership> memberships = applicationService.getActiveMembershipsOfUser(account.getName());
            model.addAttribute("memberships",memberships);
        }
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        return "index";
    }


    @GetMapping("/groupRequests/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String showGroupRequests(KeycloakAuthenticationToken token, Model model,
                                @RequestParam(name = "search") Optional search,
                                @PathVariable("id") String groupId) {

        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        ValidationResult validationResult = applicationService.isActiveAdmin(token.getName(), groupId);

        if (validationResult.isValid()) {
            model.addAttribute("groupId", groupId);
            model.addAttribute("members", applicationService.getPendingRequestOfGroup(groupId));
            return "groupRequests";
        }
        return "redirect:/gruppen1/";
    }

    @PostMapping("/groupRequests/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String manageGroupRequests(KeycloakAuthenticationToken token, Model model,
                                   @RequestParam(value="username") String username,
                                   @RequestParam(value="action") String action,
                                   @PathVariable("id") String groupId) {
        ValidationResult validationResult = applicationService.isActiveAdmin(token.getName(), groupId);

        if (validationResult.isValid()) {
            acceptOrRejectMember(token, username, action, groupId);
            return "redirect:/gruppen1/admin/{id}";
        }
        return "redirect:/gruppen1/";
    }

    private void acceptOrRejectMember(KeycloakAuthenticationToken token,
                                      @RequestParam("username") String username,
                                      @RequestParam("action") String action,
                                      @PathVariable("id") String groupId) {
        if (action.equals("accept")) {
            applicationService.acceptMembership(username, groupId, token.getName());
        } else if (action.equals("reject")) {
            applicationService.rejectMembership(username, groupId, token.getName());
        }
    }


    @GetMapping("/searchResults")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String showSearchGroupResults(KeycloakAuthenticationToken token, Model model,
                              @RequestParam(name = "search") Optional search) {
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        return "searchResults";
    }

    @PostMapping("/searchResults")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String joinPublicGroup(KeycloakAuthenticationToken token, Model model,
                                  @RequestParam(value = "id") String groupId,
                                  @RequestParam(value="action") String action){
        if(action.equals("assign")) {
            applicationService.joinGroup(token.getName(), groupId, "");
        }
        return "redirect:/gruppen1/";
    }



    @GetMapping("/requestMessage/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String showRequestMessageForm(KeycloakAuthenticationToken token, Model model,
                                   @RequestParam(name = "search") Optional search,
                                   @PathVariable("id") String groupId){
        if (search.isPresent()) {
            return searchGroups(search, model);
        }
        model.addAttribute("groupId", groupId);
        return "requestDescription";
    }

    @PostMapping("/requestMessage/{id}")
    @Secured({"ROLE_studentin", "ROLE_orga"})
    public String sendRequestMessage(KeycloakAuthenticationToken token, Model model,
                                   @RequestParam(value = "message") String message,
                                   @PathVariable("id") String groupId) {

        applicationService.joinGroup(token.getName(), groupId, message);
        return "redirect:/gruppen1/";
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
