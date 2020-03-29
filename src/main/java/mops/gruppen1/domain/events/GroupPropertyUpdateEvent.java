package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * Update GroupProperties (name, description and type).
 * <p>
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GroupPropertyUpdateEvent implements IEvent {

    private String groupId;
    private String updatedBy;
    private String groupName;
    private String description;
    private String groupType;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers,
                        HashMap<String, List<Membership>> userToMembers,
                        HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        group.setName(new GroupName(this.groupName));
        group.setDescription(new GroupDescription(this.description));
        group.setGroupType((GroupType.valueOf(this.groupType)));
    }
}
