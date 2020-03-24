package mops.gruppen1.domain;

import lombok.Getter;

import java.util.UUID;

/**
 * Representing a membership of a user within a group.
 */
@Getter
public class Membership {

    private UUID memberid;
    private User user;
    private Group group;
    private MembershipType membershipType;
    private MembershipStatus membershipStatus;

    public void setMembershipStatus(MembershipStatus membershipStatus)    {
        this.membershipStatus = membershipStatus;
    }
    
    public Membership(User user, Group group, MembershipType membershipType, MembershipStatus membershipStatus) {
        this.memberid = UUID.randomUUID();
        this.user = user;
        this.group = group;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }
}
