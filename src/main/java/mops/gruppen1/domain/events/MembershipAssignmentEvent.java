package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * For public Groups:
 * Create a Membership with Status 'ACTIVE'
 * Add Membership to groupToMembers and userToMembers and the group's list of members
 */
@AllArgsConstructor
public class MembershipAssignmentEvent implements IEvent {

    private String groupId;
    private String userName;
    private String membershipType;

    /**
     * Adds a new membership with Status ACTIVE in all datastructures
     *
     * @param groupToMembers Hashmap that maps a String(groupId) to a list of memberships.
     * @param userToMembers  Hashmap that maps a String(userId) to a list of memberships
     * @param users          Hashmap that maps a String(userId) to a user.
     * @param groups         Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        User user = users.get(userName);
        MembershipType membershipType = MembershipType.valueOf(this.membershipType);

        Membership membership = new Membership(user, group, membershipType, MembershipStatus.ACTIVE);
        group.addMember(membership);
        groupToMembers.get(groupId).add(membership);
        userToMembers.get(userName).add(membership);

    }
}
