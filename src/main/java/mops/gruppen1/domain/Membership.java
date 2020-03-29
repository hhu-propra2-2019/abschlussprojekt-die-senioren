package mops.gruppen1.domain;

import lombok.Getter;

import java.util.UUID;

/**
 * Representing a membership of a user within a group.
 */
@Getter
public class Membership {

    private UUID memberId;
    private User user;
    private Group group;
    private MembershipType membershipType;
    private MembershipStatus membershipStatus;
    private MembershipRequestMessage membershipRequestMessage;

    public void setMembershipStatus(MembershipStatus membershipStatus)    {
        this.membershipStatus = membershipStatus;
    }


    public Membership(User user, Group group, MembershipType membershipType, MembershipStatus membershipStatus) {
        this.memberId = UUID.randomUUID();
        this.user = user;
        this.group = group;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
    }


    public Membership(User user, Group group, MembershipType membershipType, MembershipStatus membershipStatus,
                      MembershipRequestMessage membershipRequestMessage) {
        this.memberId = UUID.randomUUID();
        this.user = user;
        this.group = group;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
        this.membershipRequestMessage = membershipRequestMessage;
    }


    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }
}
