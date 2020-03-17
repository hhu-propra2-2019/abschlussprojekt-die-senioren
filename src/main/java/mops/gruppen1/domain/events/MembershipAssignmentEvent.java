package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class MembershipAssignmentEvent implements Event {

    String groupId;
    String userName;
    String membershipType;


    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers,HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        User user = users.get(userName);
        Type membershipType = Type.valueOf(this.membershipType);

        Membership membership = new Membership(user,group, membershipType, Status.ACTIVE);
        groupToMembers.get(group).add(membership);
        userToMembers.get(user).add(membership);

    }
}
