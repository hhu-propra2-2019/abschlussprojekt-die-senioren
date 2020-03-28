package mops.gruppen1.controller.interaction;


import mops.gruppen1.applicationService.RestService;
import mops.gruppen1.data.daos.GroupDAO;
import mops.gruppen1.data.daos.UpdatedGroupsDAO;
import mops.gruppen1.data.daos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gruppen1")
@Secured("ROLE_api_user")
public class InteractionController {

    RestService restService;

    @Autowired
    public InteractionController(RestService restService) {
        this.restService = restService;
    }

    @GetMapping("/isUserInGroup")
    public ResponseEntity<Map<String, Boolean>> isUserInGroup(@RequestParam(value = "userName", defaultValue = "") String userName,
                                                              @RequestParam(value = "groupId", defaultValue = "") String groupId) {

        //Check if given username and groupId are not empty
        if(userName.equals("") || groupId.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //Check if user is in group
        boolean isUserInGroup = restService.isUserInGroup(userName,groupId);

        //prepare content for response statement
        Map<String,Boolean> resultMap = new HashMap<>();
        resultMap.put("isUserInGroup",isUserInGroup);

        //return result
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/isUserAdminInGroup")
    public ResponseEntity<Map<String, Boolean>> isUserAdminInGroup(@RequestParam(value = "userName", defaultValue = "") String userName,
                                                                    @RequestParam(value = "groupId", defaultValue = "") String groupId) {

        //Check if given username and groupId are not empty
        if(userName.equals("") || groupId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Check if user is admin in group
        boolean isUserAdminInGroup = restService.isUserAdminInGroup(userName,groupId);

        //prepare content for response statement
        Map<String,Boolean> resultMap = new HashMap<>();
        resultMap.put("isUserAdminInGroup", isUserAdminInGroup);

        //return result
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/doesGroupExist")
    public ResponseEntity<Map<String, Boolean>> doesGroupExist(@RequestParam(value = "groupId", defaultValue = "") String groupId) {

        //Check if given groupId is not empty
        if(groupId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Check if group does exist
        boolean doesGroupExist = restService.doesActiveGroupExist(groupId);

        //prepare content for response statement
        Map<String,Boolean> resultMap = new HashMap<>();
        resultMap.put("doesGroupExist", doesGroupExist);

        //return result
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/returnAllGroups")
    public ResponseEntity<UpdatedGroupsDAO> returnAllGroups(@RequestParam(value = "lastEventId", defaultValue = "0") Long lastEventId) {
        UpdatedGroupsDAO updatedGroupsDAO = restService.getUpdatedGroups(lastEventId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedGroupsDAO);
    }

    @GetMapping("/returnUsersOfGroup")
    public ResponseEntity<List<UserDAO>> returnUsersOfGroup(@RequestParam(value = "groupId", defaultValue = "") String groupId) {

        //Check if given groupId is not empty
        if(groupId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //get all users of group
        List<UserDAO> usersOfGroup = restService.getUsersOfGroup(groupId);

        return ResponseEntity.status(HttpStatus.OK).body(usersOfGroup);
    }

    @GetMapping("/returnGroupsOfUsers")
    public ResponseEntity<List<GroupDAO>> returnGroupsOfUsers(@RequestParam(value = "userName", defaultValue = "") String userName) {

        //Check if given userName is not empty
        if(userName.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //get all groups of user
        List<GroupDAO> groupsOfUser = restService.getGroupsOfUser(userName);

        return ResponseEntity.status(HttpStatus.OK).body(groupsOfUser);
    }








}
