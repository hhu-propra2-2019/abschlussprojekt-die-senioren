package mops.gruppen1.domain;

import java.util.List;
import java.util.UUID;

/**
 * Representing the abstract model of a group.
 */
public abstract class Group {
    private List<Membership> members;
    private UUID groupId;
    private GroupName name;
    private GroupDescription description;
}
