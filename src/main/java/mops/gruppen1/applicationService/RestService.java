package mops.gruppen1.applicationService;

import mops.gruppen1.data.DAOs.GroupDAO;
import mops.gruppen1.data.DAOs.UpdatedGroupsDAO;
import mops.gruppen1.data.EventIdOnly;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.data.GroupIdOnly;
import mops.gruppen1.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * this class manages the interaction with external systems
 * - by providing information if relevant changes have happened since the last request
 * - by sending DAOs of Groups if necessary
 */
public class RestService {

    GroupService groupService;
    EventRepo eventRepo;

    @Autowired
    public RestService(GroupService groupService, EventRepo eventRepo) {
        this.groupService = groupService;
        this.eventRepo = eventRepo;
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
        return createUpdatedGroupsDAOs(latestEventId);
    }

    private UpdatedGroupsDAO createUpdatedGroupsDAOs(Long latestEventId) {
        UpdatedGroupsDAO updatedGroupsDAO = new UpdatedGroupsDAO(latestEventId);
        List<GroupIdOnly> changedGroupIds = eventRepo.findAllByIdAfter(latestEventId);
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
