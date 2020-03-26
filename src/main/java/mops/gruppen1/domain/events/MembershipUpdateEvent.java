package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipType;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Edit MembershipType 'ADMIN' or 'VIEWER' of Membership
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class MembershipUpdateEvent implements IEvent {

    private String groupId;
    private String userName;
    private String updatedBy;
    private String updatedTo;

    public MembershipUpdateEvent(String groupId, String userName, String updatedBy) {
        this.groupId = groupId;
        this.userName = userName;
        this.updatedBy = updatedBy;
    }

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String,
            List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        //TODO Prüfe, dass ein Member sich selbst nicht ändern kann(updatedBy ungleich memberId)
        List<Membership> memberships = userToMembers.get(userName);
        Membership membership = getMembership(memberships, groupId);
        changeMembershipType(membership);
    }

    /**
     * Changes the MembershipType of a given membership.
     *
     * @param membership The membership whose type is to be changes.
     */
    private void changeMembershipType(Membership membership) {
        if (membership.getMembershipType().equals(MembershipType.VIEWER)) {
            membership.setMembershipType(MembershipType.ADMIN);
            this.updatedTo = "ADMIN";
        } else {
            membership.setMembershipType(MembershipType.VIEWER);
            this.updatedTo = "VIEWER";
        }
    }

    /**
     * finds the membership of the user in the group
     *
     * @param memberships The user's memberships
     * @param groupId     The group in which the user becomes ADMIN or VIEWER
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