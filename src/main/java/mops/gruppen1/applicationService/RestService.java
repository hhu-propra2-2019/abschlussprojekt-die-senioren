package mops.gruppen1.applicationService;

import mops.gruppen1.data.EventIdOnly;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.data.GroupIdOnly;
import mops.gruppen1.data.daos.GroupDAO;
import mops.gruppen1.data.daos.UpdatedGroupsDAO;
import mops.gruppen1.data.daos.UserDAO;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * this class manages the interaction with external systems
 * - by providing information if relevant changes have happened since the last request
 * - by sending DAOs of Groups if necessary
 */
@Service
public class RestService {

    GroupService groupService;
    EventRepo eventRepo;

    @Autowired
    public RestService(GroupService groupService, EventRepo eventRepo) {
        this.groupService = groupService;
        this.eventRepo = eventRepo;
    }

    /**
     * Check if user is member of group or not.
     * @param username
     * @param groupId
     * @return
     */
    public boolean isUserInGroup(String username, String groupId) {
        ValidationResult validationResult = groupService.isUserMemberOfGroup(username, groupId);
        return validationResult.isValid();
    }


    /**
     * Check if user is admin in given group or not.
     * @param username
     * @param groupId
     * @return
     */
    public boolean isUserAdminInGroup(String username, String groupId) {

        // When user is not a member of the group, return false
        if(!isUserInGroup(username, groupId)) return false;

        ValidationResult validationResult = groupService.isUserAdminInGroup(username, groupId);
        return validationResult.isValid();
    }

    /**
     * @param oldEventId is received from other services via RestController
     * @return updatedGroupsDAO that contains a list of GroupDAOs for the changed groups
     * and the latest eventId
     */
    public UpdatedGroupsDAO getUpdatedGroups(Long oldEventId) {
        Long latestEventId = getLatestEventId();
        if (isEventIdUpToDate(oldEventId, latestEventId)) {
            return (new UpdatedGroupsDAO(latestEventId));
        }
        return createUpdatedGroupsDAOs(oldEventId, latestEventId);
    }

    private UpdatedGroupsDAO createUpdatedGroupsDAOs(Long oldEventId, Long latestEventId) {
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(latestEventId);
        List<GroupIdOnly> changedGroupIds = eventRepo.findDistinctByIdAfter(oldEventId);
        for (GroupIdOnly groupId : changedGroupIds) {
            String id = groupId.getGroup();
            GroupDAO groupDAO = createGroupDAO(id);
            updatedGroupsDAO.addGroupDAO(groupDAO);
        }
        return updatedGroupsDAO;
    }

    /**
     * @return the id of the event that was last recently stored in the Repo
     */
    private Long getLatestEventId() {
        List<EventIdOnly> latestEventIdAsList = eventRepo.findTopByOrderByIdDesc();
        Long latestEventId = latestEventIdAsList.get(0).getId();
        return latestEventId;
    }

    private boolean isEventIdUpToDate(Long oldEventId, Long latestEventId) {
        return oldEventId.equals(latestEventId);
    }

    /**
     * creates a DAO with necessary attributes retrieved from domain group
     *
     * @param groupId is groupId
     */
    private GroupDAO createGroupDAO(String groupId) {
        Group group = groupService.getGroups().get(groupId);
        return new GroupDAO(groupId, group.getName().toString(), group.getDescription().toString(), group.getGroupStatus().toString());
    }

    public boolean doesActiveGroupExist(String groupId) {
        ValidationResult validationResult = groupService.isGroupActive(groupId);
        return validationResult.isValid();
    }


    /**
     * Retrieve all groups that a specifc user is a member of.
     * @param userName
     * @return List of GroupDAOs
     */
    public List<GroupDAO> getGroupsOfUser(String userName) {

        //get groups of user from GroupService
        List<Group> groups =  groupService.getGroupsWhereUserIsActive(userName);

        //instantiate new List of groupDAOs
        List<GroupDAO> groupDAOs = new ArrayList<>();

        //get groupId for each Group from List 'groups' and create new GroupDAOs
        groups.stream()
                .map(Group::getGroupId)
                .forEach(groupId -> groupDAOs.add(createGroupDAO(groupId.toString())));

        return groupDAOs;
    }


    /**
     * Retrieve all users of a specific group.
     * @param groupId
     * @return List of UserDAOs
     */
    public List<UserDAO> getUsersOfGroup(String groupId) {

        //get users of group from GroupService
        List<User> users =  groupService.getActiveUsersOfGroup(groupId);

        //instantiate new List of userDAOs
        List<UserDAO> userDAOs = new ArrayList<>();

        //get username for each User from List 'users' and create a new UserDAO containing the obtained username
        users.stream()
                .map(User::getUsername)
                .forEach(username -> userDAOs.add(new UserDAO(username.toString())));

        return userDAOs;
    }
}
