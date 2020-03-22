package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * For restricted Groups:
 * Create a Membership with Status 'PENDING'
 * Add Membership to groupToMembers, userToMembers and group's list of members
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MembershipRequestEvent implements IEvent {

    private String groupId;
    private String userName;
    private String membershipType;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

        // TODO: move check to Group Service ?
        // what about group existence in group, needs to be checked?
        if (!users.containsKey(userName)) {
            User newUser = new User(new Username(userName));
            users.put(userName, newUser);
        }
        // TODO: move check to Group Service ?
        if (!userToMembers.containsKey(userName)) {
            userToMembers.put(userName, new ArrayList<>());
        }
        Group group = groups.get(groupId);
        User user = users.get(userName);
        MembershipType membershipType = MembershipType.valueOf(this.membershipType);
        Membership membership = new Membership(user, group, membershipType, MembershipStatus.PENDING);

        group.addMember(membership);
        groupToMembers.get(groupId).add(membership);
        userToMembers.get(userName).add(membership);
    }
}
