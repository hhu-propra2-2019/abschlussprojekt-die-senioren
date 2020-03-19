package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * Edit Type 'ADMIN' or 'VIEWER' of Membership
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
        //Membership updater = findUpdater(groups);
        //if (deletor != null && deletor.getType().equals(Type.ADMIN)) {
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
     * Changes the Type of a given membership.
     *
     * @param membership The membership whose type is to be changes.
     */
    private void changeMembershipType(Membership membership) {
        if (membership.getType().equals(Type.ADMIN) && updatedTo.equalsIgnoreCase("VIEWER")) {
            membership.setType(Type.VIEWER);
        } else if (membership.getType().equals(Type.VIEWER) && updatedTo.equalsIgnoreCase("ADMIN")) {
            membership.setType(Type.ADMIN);
        }
    }
}
