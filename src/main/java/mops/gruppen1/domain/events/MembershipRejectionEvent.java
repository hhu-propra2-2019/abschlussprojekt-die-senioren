package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * For restricted groups and a PENDING Membership
 * Change status of Membership to 'REJECTED'
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class MembershipRejectionEvent implements IEvent {


    private String groupId;
    private String userName;

    /**
     * Sets the membership of the user to REJECTED in all datastructures
     *
     * @param groupToMembers Hashmap that maps a String(groupId) to a list of memberships.
     * @param userToMembers  Hashmap that maps a String(userId) to a list of memberships
     * @param users          Hashmap that maps a String(userId) to a user.
     * @param groups         Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        Membership membership = getMembership(memberships, groupId);
        membership.setMembershipStatus(MembershipStatus.REJECTED);
    }

    /**
     * finds the membership of the user in the group to which access in refused
     * @param memberships The user's memberships
     * @param groupId The group the user is not accepted to
     * @return the membership belonging to groupID and contained in memberships
     */
    private Membership getMembership(List<Membership> memberships, String groupId) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getGroup().getGroupId().toString().equals(groupId)) {
                membership = m;
                break;
            }
        }
        return membership;
    }
}
