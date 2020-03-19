package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * change status of Membership to 'Active'
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class MembershipAcceptanceEvent implements IEvent {

    private String groupId;
    private String userName;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        Membership membership = getMembership(memberships, group);
        membership.setStatus(Status.ACTIVE);
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
