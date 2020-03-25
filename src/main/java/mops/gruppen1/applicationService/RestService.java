package mops.gruppen1.applicationService;

import mops.gruppen1.data.DAOs.GroupDAO;
import mops.gruppen1.data.DAOs.UpdatedGroupsDAO;
import mops.gruppen1.data.EventIdOnly;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.data.GroupIdOnly;
import mops.gruppen1.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return groupService.isUserMemberOfGroup(username, groupId);
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

        return groupService.isUserAdminInGroup(username, groupId);
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
        return createUpdatedGroupsDAOs(oldEventId);
    }

    private UpdatedGroupsDAO createUpdatedGroupsDAOs(Long oldEventId) {
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(oldEventId);
        List<GroupIdOnly> changedGroupIds = eventRepo.findAllByIdAfter(oldEventId);
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
}
