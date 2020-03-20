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
    private String userName;
    private String updatedBy;
    private String updatedTo;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String,
            List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        //TODO Prüfe, dass ein Member sich selbst nicht ändern kann(updatedBy ungleich memberId)
        List<Membership> memberships = userToMembers.get(userName);
        Membership membership= getMembership(memberships, groupId);
        changeMembershipType(membership);
    }

    /**
     * Changes the MembershipType of a given membership.
     *
     * @param membership The membership whose type is to be changes.
     */
    private void changeMembershipType(Membership membership) {
        if (updatedTo.equalsIgnoreCase("VIEWER")) {
            membership.setMembershipType(MembershipType.VIEWER);
        } else if (updatedTo.equalsIgnoreCase("ADMIN")) {
            membership.setMembershipType(MembershipType.ADMIN);
        }
    }

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
