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
    private Type type;
    private Status status;

    public Membership(User user, Group group, Type type, Status status) {
        this.user = user;
        this.group = group;
        this.type = type;
        this.status = status;
    }
}
