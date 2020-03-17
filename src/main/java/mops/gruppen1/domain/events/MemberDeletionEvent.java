package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * change attribute Status in Membership to 'Deactivated'. NO deletion from datastructures
 */
public class MemberDeletionEvent implements Event {

    private String groupId;
    private String removedUserId;
    private String removedByUserId;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        findMember(groups);
    }

    private Membership findMember(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> removedUserId.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }

    private void deactiveMembershipInGroup(Membership membership)    {
        membership.setStatus(Status.DEACTIVATED);
    }
}
