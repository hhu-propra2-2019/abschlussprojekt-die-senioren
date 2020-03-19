package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.Status;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Change attribute Status in Membership to DEACTIVATED.
 * Delete Membership from Groups ?
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MemberResignmentEvent implements IEvent {

    private String groupId;
    private String leavingMemberId;

    /**
     * Deactivates the given membership in all datastructures
     *
     * @param groupToMembers Hashmap that maps a group to a list of memberships.
     * @param userToMembers  Hashmap that maps a user to a list of memberships
     * @param users          Hashmap that maps a String(userId) to a user.
     * @param groups         Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Membership leavingMember = findLeavingMember(groups);
        deactiveMembership(leavingMember);
    }

    /**
     * Finds the member that is leaving a group.
     *
     * @param groups HashMap of groups in which the member is searched for.
     * @return The member that matches the leavingMemberId.
     */
    private Membership findLeavingMember(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> leavingMemberId.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }

    /**
     * Deactivates a given membership.
     *
     * @param membership The membership that is to be deactivated.
     */
    private void deactiveMembership(Membership membership) {

        membership.setStatus(Status.DEACTIVATED);
    }
}
