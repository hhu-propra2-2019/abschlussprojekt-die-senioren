package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Representing the model of a group.
 * Has no attribute GroupStatus yet
 */
@Getter
@EqualsAndHashCode
public class Group {
    List<Membership> members;
    UUID groupId;
    GroupName name;
    GroupDescription description;
    User groupCreator;
    GroupStatus groupStatus;

    public Group (List<Membership> members, GroupName name, GroupDescription groupDescription, User groupCreator, GroupStatus groupStatus) {
       this.members = members;
       this.groupId  = UUID.randomUUID();
       this.name = name;
       this.description = groupDescription;
       this.groupCreator = groupCreator;
       this.groupStatus = groupStatus;
    }
}
