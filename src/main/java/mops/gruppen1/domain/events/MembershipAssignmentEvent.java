package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * For public Groups:
 * Create a Membership with Status 'ACTIVE'
 * Add Membership to groupToMembers and userToMembers
 */
@AllArgsConstructor
public class MembershipAssignmentEvent implements IEvent {

    private String groupId;
    private String userName;
    private String membershipType;


    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        User user = users.get(userName);
        Type membershipType = Type.valueOf(this.membershipType);

        Membership membership = new Membership(user, group, membershipType, Status.ACTIVE);
        group.addMember(membership);
        groupToMembers.get(group).add(membership);
        userToMembers.get(user).add(membership);

    }
}
