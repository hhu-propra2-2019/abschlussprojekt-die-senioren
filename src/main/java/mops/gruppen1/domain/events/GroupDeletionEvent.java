package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GroupDeletionEvent implements Event {

    Group group;
    String deletedByUser;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashSet<User> users, HashSet<Group> groups) {
        groupToMembers.remove(group);
        groups.remove(group);
        updateUserMemberships(userToMembers);

    }

    /**
     * method deletes memberships taht map to deleted groups from userToMember Hash-Map
     *
     * @param userToMembers
     */
    private void updateUserMemberships(HashMap<User, List<Membership>> userToMembers) {
        for (Map.Entry<User, List<Membership>> entry : userToMembers.entrySet()) {
            List<Membership> memberships = entry.getValue();
            List<Membership> newMemberships = memberships.stream().filter(member -> !member.getGroup().equals(group)).collect(Collectors.toList());
            entry.setValue(newMemberships);
        }
    }
}
