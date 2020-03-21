package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.GroupStatus;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
/**
 * Change attribute GroupStatus of Group to 'deactivated'. NO deletion from datastructures
 * Delete UserMemberships for members of that Group
 */
public class GroupDeletionEvent implements IEvent {

    String groupId;
    String deletedByUser;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        groupToMembers.remove(groupId);
        group.setStatus(GroupStatus.DEACTIVATED);
        updateUserMemberships(userToMembers, groups);
    }

    /**
     * method deletes memberships that map to deleted groups from userToMember Hash-Map
     *
     * @param userToMembers
     * @param groups
     */
    private void updateUserMemberships(HashMap<String, List<Membership>> userToMembers, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        for (Map.Entry<String, List<Membership>> entry : userToMembers.entrySet()) {
            List<Membership> memberships = entry.getValue();
            List<Membership> newMemberships = memberships.stream().filter(member -> !member.getGroup().equals(group)).collect(Collectors.toList());
            entry.setValue(newMemberships);
        }
    }
}
