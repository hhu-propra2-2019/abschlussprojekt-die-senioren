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
 * Add Membership to groupToMembers and userToMembers
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MembershipRequestEvent implements IEvent {

    private String groupId;
    private String userName;
    private String membershipType;

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
