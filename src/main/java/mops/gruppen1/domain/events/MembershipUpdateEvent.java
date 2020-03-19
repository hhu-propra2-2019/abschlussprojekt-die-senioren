package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * Edit MembershipType 'ADMIN' or 'VIEWER' of Membership
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MembershipUpdateEvent implements IEvent {

    private String groupId;
    private String memberId;
    private String updatedBy;
    private String updatedTo;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String,
            List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        //TODO Ziehe Suche nach Updater & Prüfung ob Admin in den Groupservice
        //TODO Prüfe, dass ein Member sich selbst nicht ändern kann(updatedBy ungleich memberId)
        //Membership updater = findUpdater(groups);
        //if (updater != null && updater.getMembershipType().equals(MembershipType.ADMIN)) {
        Membership toBeUpdated = findUpdatedMember(groups);
        changeMembershipType(toBeUpdated);
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

    /**
     * Changes the MembershipType of a given membership.
     *
     * @param membership The membership whose type is to be changes.
     */
    private void changeMembershipType(Membership membership) {
        if (membership.getMembershipType().equals(MembershipType.ADMIN) && updatedTo.equalsIgnoreCase("VIEWER")) {
            membership.setMembershipType(MembershipType.VIEWER);
        } else if (membership.getMembershipType().equals(MembershipType.VIEWER) && updatedTo.equalsIgnoreCase("ADMIN")) {
            membership.setMembershipType(MembershipType.ADMIN);
        }
    }
}
