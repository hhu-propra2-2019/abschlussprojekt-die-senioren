package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
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

    public Group (List<Membership> members, GroupName name, GroupDescription groupDescription, User groupCreator, GroupStatus groupStatus, GroupType groupType) {
       this.members = members;
       this.groupId  = UUID.randomUUID();
       this.name = name;
       this.description = groupDescription;
       this.groupCreator = groupCreator;
       this.groupStatus = groupStatus;
       this.groupType = groupType;
    }

    public void addMember(Membership membership) {
        members.add(membership);
    }

    public void removeMember(Membership membership){
        membership.setStatus(Status.DEACTIVATED);
    }

    public void setAppointment(Appointment appointment){
        this.appointment = appointment;
    }

    public void setStatus(GroupStatus groupStatus){
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
}
