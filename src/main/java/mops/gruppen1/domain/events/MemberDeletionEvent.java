package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * change attribute Status in Membership to 'Deactivated'. NO deletion from datastructures
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class MemberDeletionEvent implements IEvent {

    private String groupId;
    private String removedUserName;
    private String removedByUserName;

    /**
     * Deactivates the given membership in all datastructures.
     *
     * @param groupToMembers Hashmap that maps a String(groupId) to a list of memberships.
     * @param userToMembers  Hashmap that maps a String(userId) to a list of memberships.
     * @param users          Hashmap that maps a String(userId) to a user.
     * @param groups         Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Membership toBeDeleted = findRemovedMember(groupId, userToMembers);
        deactivateMembership(toBeDeleted);
    }

    /**
     * Finds the member that is to be removed in a group.
     *
     * @param groupId The group in which a member will be deactivated.
     * @return The membership of the member that matches the removedUserName.
     */
    private Membership findRemovedMember(String groupId, HashMap<String, List<Membership>> userToMembers) {
        List<Membership> memberships = userToMembers.get(removedUserName);
        Membership membership = memberships.stream()
                .filter(m -> m.getGroup().getGroupId().toString().equals(groupId))
                .findFirst()
                .orElse(null);

        return membership;
    }

    /**
     * Deactivates a given membership.
     *
     * @param membership The membership that is to be deactivated.
     */
    private void deactivateMembership(Membership membership) {
        membership.setMembershipStatus(MembershipStatus.DEACTIVATED);
    }
}