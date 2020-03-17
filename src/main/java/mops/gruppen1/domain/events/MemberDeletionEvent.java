package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * change attribute Status in Membership to 'Deactivated'. NO deletion from datastructures
 */
public class MemberDeletionEvent implements Event {

    private String groupId;
    private String removedMemberId;
    private String removedByMemberId;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Membership deletor = findDeletor(groups);
        if (deletor != null && deletor.getType().equals(Type.ADMIN)) {
            Membership toBeDeleted = findRemovedMember(groups);
            deactiveMembership(toBeDeleted);
            deactivateMembershipUser(userToMembers,toBeDeleted);
            deactivateMembershipGroup(groupToMembers,toBeDeleted);
        }
    }

    /**
     * Finds a member of a group.
     * @param groups The group in which a member is searched for.
     * @return The member that matches the removedUserId field.
     */
    private Membership findRemovedMember(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> removedMemberId.equals(member.getMemberid().toString()))
                .findFirst()
                .orElse(null);

        return membership;
    }

    private Membership findDeletor(HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Membership membership = group.getMembers().stream()
                .filter(member -> removedByMemberId.equals(member.getMemberid().toString()))
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