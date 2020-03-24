package mops.gruppen1.Controller.Interaction;


import mops.gruppen1.data.DAOs.GroupDAO;
import mops.gruppen1.data.DAOs.UserDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gruppen1")
public class InteractionController {

    @GetMapping("/isUserInGroup")
    public Boolean isUserInGroup(@RequestParam(value = "userName", defaultValue = "") String userName,
                                 @RequestParam(value = "groupId", defaultValue = "") String groupId) {
        // if default-value => request is not valid, username must be given
        //Check if user is in Group
        //return yes/no?
        return true;
    }

    @GetMapping("/isUserAdminInGroup")
    public Boolean isUserAdminInGroup(@RequestParam(value = "userName", defaultValue = "") String userName,
                          @RequestParam(value = "groupId", defaultValue = "") String groupId) {
        // if default-value => request is not valid, username must be given
        // check if user ist admin in Group
        //return yes/no?
        return true;
    }

    @GetMapping("/doesGroupExist")
    public Boolean doesGroupExist(@RequestParam(value = "groupId", defaultValue = "") String groupId) {
        // if default-value => request is not valid, username must be given
        // check if group exists
        //return yes/no?
        return true;
    }

    @GetMapping("/returnAllGroups")
    public List<GroupDAO> returnAllGroups() {
        List<GroupDAO> groupList = new ArrayList<GroupDAO>();
        // return List of GroupDAOs
        return groupList;
    }

    @GetMapping("/returnUsersOfGroup")
    public List<UserDAO> returnUsersOfGroup(@RequestParam(value = "groupId", defaultValue = "") String groupId) {
        List<UserDAO> userList = new ArrayList<UserDAO>();
        // if default-value => request is not valid, username must be given
        // return all UserDAOs of Group
        return userList;
    }

    @GetMapping("/returnGroupsOfUsers")
    public List<GroupDAO> returnGroupsOfUsers(@RequestParam(value = "userName", defaultValue = "") String userName) {
        List<GroupDAO> groupList = new ArrayList<GroupDAO>();
        // if default-value => request is not valid, username must be given
        // return all groupDAOs a user belongs to
        return groupList;
    }








}
