package mops.gruppen1.domain;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Representing the abstract model of a group.
 */

@Getter
public abstract class Group {
    List<Membership> members;
    UUID groupId;
    GroupName name;
    GroupDescription description;
}
