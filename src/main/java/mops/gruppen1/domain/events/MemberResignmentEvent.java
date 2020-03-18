package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Change attribute Status in Membership to DEACTIVATED.
 * Delete Membership from Groups ?
 */
public class MemberResignmentEvent implements IEvent {

    private String groupId;
    private String leavingMemberId;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

    }

    /**
     *  Finds the member that is leaving a group.
     * @param groups HashMap of groups in which the member is searched for.
     * @return The member that matches the leavingMemberId.
     */
    private Membership findLeavingMember(HashMap<String,Group> groups)  {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> leavingMemberId.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }
}
