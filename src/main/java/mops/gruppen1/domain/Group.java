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
    private Appointment appointment;
    private Material material;
    private Forum forum;
    private Assignment assignment;
    private Module module;

    public Group(List<Membership> members, GroupName name, GroupDescription groupDescription, User groupCreator, GroupStatus groupStatus, GroupType groupType) {
        this.members = members;
        this.groupId = UUID.randomUUID();
        this.name = name;
        this.description = groupDescription;
        this.groupCreator = groupCreator;
        this.groupStatus = groupStatus;
        this.groupType = groupType;
        this.module = new Module();
        this.module.setModulename(new Modulename("Keine Veranstaltung."));
    }

    public Group(List<Membership> members, String groupId, GroupName name, GroupDescription description, User groupCreator, GroupStatus groupStatus, GroupType groupType) {
        this.members = members;
        this.groupId = UUID.fromString(groupId);
        this.name = name;
        this.description = description;
        this.groupCreator = groupCreator;
        this.groupStatus = groupStatus;
        this.groupType = groupType;
        this.module = new Module();
        this.module.setModulename(new Modulename("Keine Veranstaltung."));
    }

    public Group(List<Membership> members, GroupName name, GroupDescription description, User groupCreator, GroupStatus groupStatus, GroupType groupType, Module module) {
        this.members = members;
        this.groupId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.groupCreator = groupCreator;
        this.groupStatus = groupStatus;
        this.groupType = groupType;
        this.module = module;
    }

    public Group(List<Membership> members, String groupId, GroupName name, GroupDescription description, User groupCreator, GroupStatus groupStatus, GroupType groupType, Module module) {
        this.members = members;
        this.groupId = UUID.fromString(groupId);
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

    public void removeMember(Membership membership) {
        membership.setMembershipStatus(MembershipStatus.DEACTIVATED);
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
