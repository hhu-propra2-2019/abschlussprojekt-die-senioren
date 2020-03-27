package mops.gruppen1.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Representing the model of a group.
 */
@Getter
@EqualsAndHashCode
public class Group {
    private List<Membership> members;
    private UUID groupId;
    private GroupName name;
    private GroupDescription description;
    private User groupCreator;
    private GroupStatus groupStatus;
    private GroupType groupType;
    private Module module;

    public Group(List<Membership> members, GroupName name, GroupDescription groupDescription, User groupCreator, GroupStatus groupStatus, GroupType groupType, Module module) {
        this.members = members;
        this.groupId = UUID.randomUUID();
        this.name = name;
        this.description = groupDescription;
        this.groupCreator = groupCreator;
        this.groupStatus = groupStatus;
        this.groupType = groupType;
        this.module = module;
    }

    public Group(List<Membership> members, UUID groupId, GroupName name, GroupDescription description, User groupCreator, GroupStatus groupStatus, GroupType groupType, Module module) {
        this.members = members;
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.groupCreator = groupCreator;
        this.groupStatus = groupStatus;
        this.groupType = groupType;
        this.module = module;
    }

    public void addMember(Membership membership) {
        members.add(membership);
    }

    public void setStatus(GroupStatus groupStatus) {
        this.groupStatus = groupStatus;
    }

    public void setName(GroupName name) {
        this.name = name;
    }

    public void setDescription(GroupDescription description) {
        this.description = description;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

}
