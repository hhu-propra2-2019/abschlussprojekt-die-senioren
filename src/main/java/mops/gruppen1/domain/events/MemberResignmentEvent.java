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
     * @param groupToMembers Hashmap that maps a group to a list of memberships.
     * @param userToMembers Hashmap that maps a user to a list of memberships
     * @param users Hashmap that maps a String(userId) to a user.
     * @param groups Hashmap that maps a String(groupId) to memberships within the group.
     */
    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Membership leavingMember = findLeavingMember(groups);
        deactiveMembership(leavingMember);
        deactivateMembershipGroup(groupToMembers,leavingMember);
        deactivateMembershipUser(userToMembers,leavingMember);
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

    /**
     * Deactivates a given membership.
     * @param membership The membership that is to be deactivated.
     */
    private void deactiveMembership(Membership membership)    {

        membership.setStatus(Status.DEACTIVATED);
    }

    /**
     * Deactivates the deleted,related membership of a user in userToMembers HashMap.
     * @param userToMembers
     * @param membership
     */
    private void deactivateMembershipUser(HashMap<User, List<Membership>> userToMembers,Membership membership)  {
        User user = membership.getUser();
        List<Membership> memberships = userToMembers.get(user);
        for(Membership member : memberships)    {
            if (member.equals(membership))  {
                member.setStatus(Status.DEACTIVATED);
            }
        }
    }

    /**
     * Deactivates the membership in the groupToMembers - Hashmap
     * @param groupToMembers Hashmap that maps groups to a list of memberships.
     * @param membership The membership that is to be deactivated.
     */
    private void deactivateMembershipGroup(HashMap<Group, List<Membership>> groupToMembers, Membership membership)  {
        Group group = membership.getGroup();
        List<Membership> membershipsGroup = group.getMembers();
        for(Membership member: membershipsGroup)    {
            if(member.equals(membership))    {
                member.setStatus(Status.DEACTIVATED);
            }
        }
    }
}
