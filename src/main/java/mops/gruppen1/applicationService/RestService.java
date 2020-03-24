package mops.gruppen1.applicationService;

import mops.gruppen1.data.DAOs.CurrentStateDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class RestService {

    @Autowired
    GroupService groupService;

    public CurrentStateDAO getUpdatedGroups(long oldEventId) {
        long latestEventId = getLatestEventId();
        if(isEventIdUpToDate(oldEventId,latestEventId)) {
            return new CurrentStateDAO(oldEventId);
        }
        return new CurrentStateDAO(latestEventId);
    }

    private long getLatestEventId() {
        //TODO:Implement real code
    return 1L;
    }

    private boolean isEventIdUpToDate(long oldEventId, long latestEventId) {
        if(oldEventId == latestEventId) return true;

        return false;
    }

    //TODO: Return new GroupDAO
    private void createGroupDAO(String groupId) {
        groupService.getGroups();
    }

}
