package mops.gruppen1.data.DAOs;


import java.util.ArrayList;
import java.util.List;

/**
 * aggregated object for RestController, wrapping all changed groupDAOs with respective latest eventId
 */

public class CurrentStateDAO {
    private List<GroupDAO> groupDAOs = new ArrayList<>();
    private long eventId;

    public CurrentStateDAO(long eventId) {
        this.eventId = eventId;
    }

    public void addGroupDAO(GroupDAO groupDAO) {
        groupDAOs.add(groupDAO);
    }
}
