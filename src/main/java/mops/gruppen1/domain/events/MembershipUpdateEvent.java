package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Edit Type 'ADMIN' or 'VIEWER' of Membership
 */
public class MembershipUpdateEvent implements IEvent {

    private String groupId;
    private String memberId;
    private String updatedBy;
    private String updatedTo;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

    }

    /**
     * Finds the member that is to be updated in a group.
     *
     * @param groups The group in which a member is searched for.
     * @return The member that matches the memberId.
     */
    private Membership findUpdatedMember(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> memberId.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }

    /**
     * Finds the member that is updating a member.
     *
     * @param groups The group in which a member is searched for.
     * @return The member that matches updatedBy.
     */
    private Membership findUpdater(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> updatedBy.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }
}
