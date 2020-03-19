package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * change status of Membership to 'REJECTED'
 */
public class MembershipRejectionEvent implements IEvent {


    private String groupId;
    private String userName;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(user);
        Group group = groups.get(groupId);

        Membership membership = getMembership(memberships, group);
        membership.setStatus(Status.REJECTED);
    }

    private Membership getMembership(List<Membership> memberships, Group group) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getGroup().equals(group)) {
                membership = m;
                break;
            }
        }
        return membership;
    }
}
