package mops.gruppen1.domain.events;

import mops.gruppen1.applicationService.GroupService;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
/**
 * Change attribute GroupStatus of Group to 'deactivated'. NO deletion from datastructures
 */
public class GroupDeletionEvent implements Event {


    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

    /**
     * method deletes memberships that map to deleted groups from userToMember Hash-Map
     * @param userToMembers
     * @param groups
     */
    private void updateUserMemberships(HashMap<User, List<Membership>> userToMembers, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        for (Map.Entry<User, List<Membership>> entry : userToMembers.entrySet()) {
            List<Membership> memberships = entry.getValue();
            List<Membership> newMemberships = memberships.stream().filter(member -> !member.getGroup().equals(group)).collect(Collectors.toList());
            entry.setValue(newMemberships);
        }
    }
}
