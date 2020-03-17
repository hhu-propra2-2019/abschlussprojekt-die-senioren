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

    public void setStatus(Status status)    {
        this.status = status;
    }
}
