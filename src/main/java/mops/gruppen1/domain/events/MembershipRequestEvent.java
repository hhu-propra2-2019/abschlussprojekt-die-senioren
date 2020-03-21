package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * For restricted Groups:
 * Create a Membership with Status 'PENDING'
 * Add Membership to groupToMembers, userToMembers and group's list of members
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MembershipRequestEvent implements IEvent {

    private String groupId;
    private String userName;
    private String membershipType;

    /**
     * Adds a new membership with Status PENDING in all datastructures
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

        Membership membership = new Membership(user, group, membershipType, MembershipStatus.PENDING);
        group.addMember(membership);
        groupToMembers.get(group).add(membership);
        userToMembers.get(user).add(membership);

    }
}
