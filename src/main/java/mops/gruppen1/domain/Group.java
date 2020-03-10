package mops.gruppen1.domain;

import java.util.List;
import java.util.UUID;

/**
 * Representing the abstract model of a group.
 */
public abstract class Group {
    List<Membership> members;
    UUID groupId;
    GroupName name;
    GroupDescription description;
}
