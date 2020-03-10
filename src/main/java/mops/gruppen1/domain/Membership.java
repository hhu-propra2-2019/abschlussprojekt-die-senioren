package mops.gruppen1.domain;

import java.util.UUID;

/**
 * Representing a membership of a user within a group.
 */
public class Membership {

    private UUID memberid;
    private User user;
    private Group group;
    private Type type;
    private Status status;
}
