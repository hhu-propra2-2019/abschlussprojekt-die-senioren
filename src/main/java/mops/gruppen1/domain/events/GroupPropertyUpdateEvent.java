package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * Edit attributes ‘name' or 'description' of a group
 */
public class GroupPropertyUpdateEvent implements IEvent {

    private String groupId;
    private String updatedBy;
    private String name;
    private String description;
    private String groupType;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        group.setName(new GroupName(this.name));
        group.setDescription(new GroupDescription(this.description));
        group.setGroupType((GroupType.valueOf(this.groupType)));
    }
}
