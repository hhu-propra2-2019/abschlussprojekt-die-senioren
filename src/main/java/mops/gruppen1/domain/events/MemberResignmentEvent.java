package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
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
    private String leavingUserName;

    /**
     * Deactivates the given membership in all datastructures
     *
     * @param groupToMembers Hashmap that maps a String(groupId) to a list of memberships.
     * @param userToMembers  Hashmap that maps a String(userId) to a list of memberships
     * @param users          Hashmap that maps a String(userId) to a user.
     * @param groups         Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        List<Membership> memberships = userToMembers.get(leavingUserName);
        Group group = groups.get(groupId);

        Membership leavingMember = getMembership(memberships, groupId);
        deactivateMembership(leavingMember);
    }

    /**
     * Deactivates a given membership.
     *
     * @param membership The membership that is to be deactivated.
     */
    private void deactivateMembership(Membership membership) {

        membership.setMembershipStatus(MembershipStatus.DEACTIVATED);
    }

    /**
     * finds the membership of the user in the group they are leaving
     *
     * @param memberships The user's memberships
     * @param groupId     The Group the user is leaving
     * @return the membership belonging to groupID and contained in memberships
     */
    private Membership getMembership(List<Membership> memberships, String groupId) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getGroup().getGroupId().toString().equals(groupId)) {
                membership = m;
                break;
            }
        }
        return membership;
    }
}